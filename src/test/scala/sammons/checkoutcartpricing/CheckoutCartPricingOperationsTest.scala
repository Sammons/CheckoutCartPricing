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

}