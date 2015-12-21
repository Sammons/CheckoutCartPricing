package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CheckoutCartPricingTypes.Cart

class CustomBundle(qualificationMatcher: (Cart) => Boolean, savingsCalculator: (Cart) => BigDecimal) extends Bundle {

  def itemsDoQualifyForBundle(cart: Cart): Boolean = qualificationMatcher(cart)

  /* Note that if a bundle needs to be applied multiple times, the total savings should be
   * reflected from a single call to this method. */
  def calculateSavings(cart: Cart): BigDecimal = savingsCalculator(cart)
}
