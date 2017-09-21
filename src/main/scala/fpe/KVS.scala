package fpe

import scalaz._, Scalaz._

sealed trait KVS[V, A]
case class Put[V](k: Long, v: V) extends KVS[V, Unit]

object KVS {
  class Ops[V, S[_]](implicit s: KVS[V, ?] :<: S) {
    def put(k: Long, v: V): Free[S, Unit] =
      Free.liftF(s.inj(Put[V](k, v)))
  }
}

