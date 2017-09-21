package fpe

import scalaz._, Scalaz._

package object interplus {
  sealed abstract class :+:[F[_], G[_]] {
    type λ[A] = Coproduct[F, G, A]
  }
  implicit class EnrichNT[F[_], H[_]](f: F ~> H) {
    def :+:[G[_]](g: G ~> H) = λ[(G :+: F)#λ ~> H](_.run.fold(g, f))
  }
}

