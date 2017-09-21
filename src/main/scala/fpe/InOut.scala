package fpe

import fpe.interplus._
import scalaz._, Scalaz._, concurrent.Task

sealed trait InOut[A]
case object GetLine extends InOut[String]
case class PrintLine(line: String) extends InOut[Unit]

object InOut {
  val interpreter: InOut ~> Task = new (InOut ~> Task) {
    def apply[A](from: InOut[A]) = from match {
      case GetLine => Task.delay {
        scala.io.StdIn.readLine()
      }
      case PrintLine(line) => Task.delay {
        println(line)
      }
    }
  }

  class Ops[S[_]](implicit s: InOut :<: S) {
    def getLine: Free[S, String] = Free.liftF(s.inj(GetLine))
    def printLine(line: String): Free[S, Unit] = Free.liftF(s.inj(PrintLine(line)))
  }
}

sealed trait Logger[A]
case class Info(line: String) extends Logger[Unit]
case class Warn(line: String) extends Logger[Unit]

object Logger {

  class Ops[S[_]](implicit s: Logger :<: S) {
    def info(line: String): Free[S, Unit] = Free.liftF(s.inj(Info(line)))
    def warn(line: String): Free[S, Unit] = Free.liftF(s.inj(Warn(line)))
  }

  val interpreter: Logger ~> Task = new (Logger ~> Task) {
    def apply[A](from: Logger[A]) = from match {
      case Info(v) => Task.delay {
        println(s"INFO: $v")
      }
      case Warn(v) => Task.delay {
        println(s"WARN: $v")
      }
    }
  }
}

object Sandbox {
  def main(args: Array[String]): Unit = {
    type Eff[A] = Coproduct[InOut, Logger, A]

    val inOutOps = new InOut.Ops[Eff]
    val loggerOps = new Logger.Ops[Eff]

    val program: Free[Eff, String] = for {
      _ <- inOutOps.printLine("Hi, what is your name?")
      name <- inOutOps.getLine
      _ <- loggerOps.info(s"name is $name")
      _ <- inOutOps.printLine(s"Hello $name")
    } yield name

    val int: Eff ~> Task =
      InOut.interpreter :+: Logger.interpreter

    program.foldMap(int).unsafePerformSync
  }
}
