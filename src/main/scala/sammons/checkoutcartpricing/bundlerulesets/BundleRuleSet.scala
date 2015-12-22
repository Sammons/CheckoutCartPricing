package sammons.checkoutcartpricing.bundlerulesets

import sammons.checkoutcartpricing.CartType.Cart
import sammons.checkoutcartpricing.CheckoutBundle

trait BundleRuleSet {

  val id: String

  def calculatePossibleBundles(cart: Cart): Set[CheckoutBundle]

}
