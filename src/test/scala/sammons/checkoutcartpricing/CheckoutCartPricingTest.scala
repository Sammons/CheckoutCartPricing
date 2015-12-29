package sammons.checkoutcartpricing

import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import sammons.checkoutcartpricing.bundlerulesets.{FlatRateSavingsRuleSet, BuyAtIncreasingDiscountForUpToMaxSavingsPerItemRuleSet, BundleRuleSet}
class CheckoutCartPricingTest extends Specification with Mockito {

  trait Setup extends Scope {
    val catalog = Seq(CatalogItem("", "apples", 1.0))

    val testIncreasingDiscountRuleSet = new BuyAtIncreasingDiscountForUpToMaxSavingsPerItemRuleSet("", catalog.head, 0.01, 0.05)
    val testFlatRateRuleSet = new FlatRateSavingsRuleSet("flat", Map(catalog.head -> 60), 25)
    val testUselessFlatRateBundle = new FlatRateSavingsRuleSet("useless", Map(catalog.head -> 1), 0)
    val bundles = Seq(testIncreasingDiscountRuleSet, testFlatRateRuleSet, testUselessFlatRateBundle)
    val checkoutCartPricing = new CheckoutCartPricing(catalog, bundles)
  }

  "calculateCheapestCheckoutCart" should {
    "calculate a result cart identical to the input cart" in new Setup {
      val testCart = Map[CatalogItem, Int](catalog.head -> 100)
      val result = checkoutCartPricing.calculateCheapestCheckoutCart(testCart)
      result.cart must_== testCart
    }

    "choose fewer bundles when more bundles would cause the same savings" in new Setup {
      val testCart = Map[CatalogItem, Int](catalog.head -> 100)
      val result = checkoutCartPricing.calculateCheapestCheckoutCart(testCart)
      result.applicableBundles.length must_== 2
    }

    "ignore bundles that aren't useful" in new Setup {
      val testCart = Map[CatalogItem, Int](catalog.head -> 100)
      val result = checkoutCartPricing.calculateCheapestCheckoutCart(testCart)
      result.applicableBundles.map(_.bundleRuleSetId).contains("useless") must_== false
    }

    "reapply a bundle twice if it is beneficial" in new Setup {
      val testCart = Map[CatalogItem, Int](catalog.head -> 120)
      val result = checkoutCartPricing.calculateCheapestCheckoutCart(testCart)
      result.applicableBundles.count(_.bundleRuleSetId == "flat") must_== 2
    }

    "calculate the correct savings amount" in new Setup {
      val testCart = Map[CatalogItem, Int](catalog.head -> 120)
      val result = checkoutCartPricing.calculateCheapestCheckoutCart(testCart)
      result.total must_== 70
    }
  }

}