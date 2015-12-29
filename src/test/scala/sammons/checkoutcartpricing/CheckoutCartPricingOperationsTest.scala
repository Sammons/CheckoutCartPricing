package sammons.checkoutcartpricing

import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import CartType._
class CheckoutCartPricingOperationsTest extends Specification with Mockito {

  trait Setup extends Scope {
    val apple = CatalogItem("1", "apple", 1)
    val orange = CatalogItem("2", "orange", 1)
    val twoAppleCart = Map[CatalogItem, Int](apple -> 2)
    val oneAppleCart = Map[CatalogItem, Int](apple -> 1)
    val emptyCart = Map[CatalogItem, Int]()
  }

  "plus" should {
    "initialize keys in the left that weren't there before, but exist on the right" in new Setup {
      emptyCart plus oneAppleCart must_== oneAppleCart
    }
    "add keys together if exist in both" in new Setup {
      oneAppleCart plus oneAppleCart must_== twoAppleCart
    }
    "do nothing to keys that do not exist in the righthand set" in new Setup {
      oneAppleCart plus emptyCart must_== oneAppleCart
    }
  }
  "minus" should {
    "not initialize keys that weren't there before (no such thing as negatives)" in  new Setup {
      emptyCart minus oneAppleCart must_== emptyCart
    }
    "remove keys that hit zero" in new Setup {
      oneAppleCart minus oneAppleCart must_== emptyCart
    }
    "subtract from counts" in new Setup {
      twoAppleCart minus oneAppleCart must_== oneAppleCart
    }
  }

  "belongTo" should {
    "return false if all keys exist but amounts are insufficient" in new Setup {
      twoAppleCart belongTo oneAppleCart must_== false
    }
    "return true if more keys exist than needed" in new Setup {
      val appleAndOrangeCart = Map[CatalogItem, Int](apple -> 1, orange -> 1)
      oneAppleCart belongTo appleAndOrangeCart must_== true
    }
    "return true if all keys exist and amounts are sufficient" in new Setup {
      oneAppleCart belongTo twoAppleCart must_== true
    }
  }
}