package sammons.checkoutcartpricing.bundlerulesets

import sammons.checkoutcartpricing.CartType.Cart
import sammons.checkoutcartpricing.CheckoutBundle

trait BundleRuleSet {

  val id: String
  /* it is a bundle ruleset's job to identify all possible sets of items
   * contained by the cart that would be possible bundle applications
   * e.g. a $0.01 off all apples per apple after 2 apples needs to include
   * 2, 3, 4, 5 ... n apples as independent CheckoutBundles.
   */
  def calculatePossibleBundles(cart: Cart): Set[CheckoutBundle]

}
