package sammons.checkoutcartpricing

import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class CheckoutCartPricingTest extends Specification with Mockito {
  sequential
  /* concurrent tests will mess with the catalog - if the catalog were not changing, concurrent tests would be fine */

  trait Setup extends Scope {

  }

  "calculateCheapestCheckoutCart" should {
    "not stack bundles" in {}
    "apply bundles repeatedly" in {}
  }

}