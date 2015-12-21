package sammons.checkoutcartpricing

import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import sammons.checkoutcartpricing.CheckoutCartPricingExceptions.EmptyCatalogException

class CheckoutCartPricingTest extends Specification with Mockito {
  sequential
  /* concurrent tests will mess with the catalog - if the catalog were not changing, concurrent tests would be fine */

  trait Setup extends Scope {
    val testCatalog = Seq(
      CatalogItem("1", "apple", 1.25),
      CatalogItem("2", "orange", 1.30),
      CatalogItem("3", "pens", 1.50),
      CatalogItem("4", "snickers", 2)
    )

    val emptyCart = Map[CatalogItem, Int]()
    val snickerCart = Map[CatalogItem, Int](CatalogItem("4", "snickers", 2) -> 1000)

    val mockBundle = mock[Bundle]

    mockBundle.itemsDoQualifyForBundle(snickerCart) returns true

    val testBundles = Seq(mockBundle)
  }

  "calculateCheckoutCartPrice" should {

    "throw EmptyCatalogException when the catalog is empty" in new Setup {
      CheckoutCartPricingImpl.initializeCheckoutCartPriceCalculationSystem(Seq(), Seq())

      CheckoutCartPricingImpl.calculateCheckoutCartPrice(emptyCart) must throwA(EmptyCatalogException())
    }

    "return the full cost when no bundles are used in initialization" in new Setup {
      CheckoutCartPricingImpl.initializeCheckoutCartPriceCalculationSystem(testCatalog, Seq())

      CheckoutCartPricingImpl.calculateCheckoutCartPrice(snickerCart) must_=== 2000.00
    }

    "return the full cost when no bundles apply" in new Setup {
      mockBundle.itemsDoQualifyForBundle(snickerCart) returns false
      CheckoutCartPricingImpl.initializeCheckoutCartPriceCalculationSystem(testCatalog, testBundles)

      CheckoutCartPricingImpl.calculateCheckoutCartPrice(snickerCart) must_=== 2000.00

      there was one (mockBundle).itemsDoQualifyForBundle(snickerCart)
      there was no (mockBundle).calculateSavings(snickerCart)
    }

    "return the reduced cost when bundles apply" in new Setup {
      mockBundle.calculateSavings(snickerCart) returns 1500
      CheckoutCartPricingImpl.initializeCheckoutCartPriceCalculationSystem(testCatalog, testBundles)

      CheckoutCartPricingImpl.calculateCheckoutCartPrice(snickerCart) must_=== 500.00

      there was one (mockBundle).itemsDoQualifyForBundle(snickerCart)
      there was one (mockBundle).calculateSavings(snickerCart)
    }

    "return zero when savings would take the price negative" in new Setup {
      mockBundle.calculateSavings(snickerCart) returns 10000
      CheckoutCartPricingImpl.initializeCheckoutCartPriceCalculationSystem(testCatalog, testBundles)

      CheckoutCartPricingImpl.calculateCheckoutCartPrice(snickerCart) must_=== 0

      there was one (mockBundle).calculateSavings(snickerCart)
    }
  }

}