package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CartType.Cart

case class CheckoutBundle(itemsInBundle: Cart, savings: BigDecimal, bundleRuleSetId: String)
