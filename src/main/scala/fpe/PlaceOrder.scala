package fpe

import fpe.interplus._
import scalaz._, Scalaz._, concurrent.Task

case class Basket()
case class Product(price: Double, name: String)
case class Address(street: String)
case class Order(
  products: List[Product],
  totalValue: Double,
  address: Address)

sealed trait PlaceOrder[A]
case object CreateBasket extends PlaceOrder[Long]
case class Add(basketId: Long, product: Product) extends PlaceOrder[Unit]
case class Place(basketId: Long, address: Address) extends PlaceOrder[Order]

object PlaceOrder {

  class Ops[S[_]](implicit s: PlaceOrder :<: S) {
    def createBasket: Free[S, Long] =
      Free.liftF(s.inj(CreateBasket))
    def add(basketId: Long, product: Product): Free[S, Unit] =
      Free.liftF(s.inj(Add(basketId, product)))
    def place(basketId: Long, address: Address): Free[S, Order] =
      Free.liftF(s.inj(Place(basketId, address)))
  }

  object Ops {
    def apply[S[_]](implicit s: PlaceOrder :<: S): Ops[S] = new Ops 
  }

  def intenterpreter[S[_]](implicit
    baskets: KVS.Ops[Basket, S],
    ms: MonotonicSeq.Ops[S]
  ): PlaceOrder ~> Free[S, ?] = new (PlaceOrder ~> Free[S, ?]) {
    def apply[A](from: PlaceOrder[A]) = from match {
      case CreateBasket =>
        for {
          k <- ms.next
          _ <- baskets.put(k, Basket())
        } yield ()
        //...
    }
  }
}


