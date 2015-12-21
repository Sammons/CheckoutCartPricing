package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CheckoutCartPricingTypes.Cart


trait Bundle {
  /* Given a cart of items and their corresponding quantities (as a map), returns whether or not this bundle may be applied */
  def itemsDoQualifyForBundle(cart: Cart): Boolean

  /* Given a cart of items and their corresponding quantities (as a map), returns the maximum savings available for this cart.
  *  Note that a bundle may be applied multiple times to a cart depending on implementation. The total savings should be
  *  reflected from a single call to this method. */
  def calculateSavings(cart: Cart): BigDecimal
}
