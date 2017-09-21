package fpe

import scalaz._, Scalaz._, concurrent.Task
import org.scalatest.FunSuite

class PlacingOrderSpec extends FunSuite {

  test("bla") {

    import PlaceOrder.Ops

    val pr1 = Product(price = 10.0, name = "szimano mis")
    val pr2 = Product(price = 20.0, name = "jan pustelnik -  manual")
    val address =  Address(street = "Zamkowa 7")

    val order: Free[PlaceOrder, Order] = for {
      // req1
      basketId <- Ops[PlaceOrder].createBasket
      // req2
      _  <- Ops[PlaceOrder].add(basketId, pr1)
      _  <- Ops[PlaceOrder].add(basketId, pr2)
      order <- Ops[PlaceOrder].place(basketId, address)
    } yield order
/*
    order.products should be(List(pr1, pr2))
    order.totalValue should be(30.0)
    order.address = address
 */



  }


}
