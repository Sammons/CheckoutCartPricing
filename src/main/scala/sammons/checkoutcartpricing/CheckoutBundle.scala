package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CartType.Cart

/* This class represents the items included in a bundle, the id of the bundle ruleset appliet,
 * and the net savings from the bundling.
 */
case class CheckoutBundle(itemsInBundle: Cart, savings: BigDecimal, bundleRuleSetId: String) {
  val savingsPerItem: BigDecimal = savings / itemsInBundle.values.sum
}
