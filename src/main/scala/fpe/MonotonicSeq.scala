package fpe

import scalaz._ Scalaz._

sealed trait MonotonicSeq[A]
case class Next extends MonotonicSeq[Long]

object MonotonicSeq {

  class Ops[S[_]](implicit MS: MonotonicSeq :<: S) {
    def next: Free[S, Long] = Free.liftF(s.inj(Next))
  }
}
