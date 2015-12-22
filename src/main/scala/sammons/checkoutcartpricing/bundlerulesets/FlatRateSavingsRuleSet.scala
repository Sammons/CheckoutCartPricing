package sammons.checkoutcartpricing.bundlerulesets

import sammons.checkoutcartpricing.{CatalogItem, CheckoutBundle}
import sammons.checkoutcartpricing.CartType.Cart

/* can be used to implement buy 1 get 1 type savings. For example a subset cart with 1 apple, and 1 orange
 * could represent buying 1 apple getting 1 orange free.
 */
class FlatRateSavingsRuleSet(ruleSetId: String, subsetCart: Cart, savings: BigDecimal) extends BundleRuleSet {
  val id = ruleSetId

  override def calculatePossibleBundles(cart: Cart): Set[CheckoutBundle] = {
    Set(CheckoutBundle(subsetCart, savings, id))
  }
}
