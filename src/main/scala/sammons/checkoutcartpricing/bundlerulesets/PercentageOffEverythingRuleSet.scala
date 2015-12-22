package sammons.checkoutcartpricing.bundlerulesets

import sammons.checkoutcartpricing.CartType._
import sammons.checkoutcartpricing.CheckoutBundle

/* can be used to implement buy X,Y,Z and get 20% off everything type savings.
 * takes 20% off the raw total value of the cart.
 */
class PercentageOffEverythingRuleSet(ruleSetId: String, subsetCartItems: Cart, savingsPercentage: BigDecimal) extends BundleRuleSet {
  val id = ruleSetId

  override def calculatePossibleBundles(cart: Cart): Set[CheckoutBundle] = {
    if (subsetCartItems belongTo cart) Set(CheckoutBundle(subsetCartItems, savingsPercentage * cart.price, ruleSetId))
    else Set()
  }
}
