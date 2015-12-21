package sammons.checkoutcartpricing

import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable._
import sammons.checkoutcartpricing.CheckoutCartPricingTypes.Cart

class CustomBundleTest extends Specification with Mockito {

  trait Setup extends Scope {

    val qualificationMatcher = (c: Cart) => if (c.isEmpty) true else false
    val savingsCalculator = (c: Cart) => BigDecimal.apply(1.234)
    val customBundle = new CustomBundle(qualificationMatcher, savingsCalculator)
  }

  "itemsDoQualifyForBundle" should {
    "invoke the qualificationMatcher" in new Setup {
      val testCart = Map[CatalogItem, Int]()
      val testNonEmptyCart = Map[CatalogItem, Int](CatalogItem("","",1) -> 1)
      customBundle.itemsDoQualifyForBundle(testCart) must_== true
      customBundle.itemsDoQualifyForBundle(testNonEmptyCart) must_== false
    }
  }

  "calculateSavings" should {
    "invoke the savingsCalculator" in new Setup {
      val testCart = Map[CatalogItem, Int]()

      customBundle.calculateSavings(testCart) must_=== 1.234
    }
  }

}
