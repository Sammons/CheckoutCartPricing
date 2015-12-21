package sammons.checkoutcartpricing

import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class CheckoutCartPricingOperationsTest extends Specification with Mockito {

  trait Setup extends Scope {
    val cartA = Map[CatalogItem, Int](
      CatalogItem("1", "apple", 1) -> 5,
      CatalogItem("2", "orange", 1) -> 1
    )

    val unchangedCartA = Map[CatalogItem, Int](
      CatalogItem("1", "apple", 1) -> 5,
      CatalogItem("2", "orange", 1) -> 1
    )

    val cartAWithOneLessApple = Map[CatalogItem, Int](
      CatalogItem("1", "apple", 1) -> 4,
      CatalogItem("2", "orange", 1) -> 1
    )

    val cartAWithNoApples = Map[CatalogItem, Int](
      CatalogItem("2", "orange", 1) -> 1
    )
  }

  "subtractCarts" should {
    "not result in negative or zero counts" in new Setup {
      val cartB = Map[CatalogItem, Int](CatalogItem("1", "apple", 1) -> 5)

      val resultCart = CheckoutCartPricingOperations.subtractCarts(cartA, cartB)

      resultCart must_== cartAWithNoApples
    }
    "ignore a key if it is not in the cart being subtracted from" in new Setup {
      val cartB = Map[CatalogItem, Int](CatalogItem("1", "juice", 1) -> 1)

      val resultCart = CheckoutCartPricingOperations.subtractCarts(cartA, cartB)

      resultCart must_== unchangedCartA
    }
    "subtract the count of a key if it exists in both carts" in new Setup {
      val cartB = Map[CatalogItem, Int](CatalogItem("1", "apple", 1) -> 1)

      val resultCart = CheckoutCartPricingOperations.subtractCarts(cartA, cartB)

      resultCart must_== cartAWithOneLessApple
    }
  }

}