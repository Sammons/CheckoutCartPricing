package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CheckoutCartPricingTypes.Cart

import scala.annotation.tailrec

/* flat rate savings are really common, so this class is provided for convenience */
class FlatRateBundle(qualifyingCart: Cart, flatRateSavings: BigDecimal) extends Bundle {

  def itemsDoQualifyForBundle(cart: Cart): Boolean = {
    qualifyingCart.keys.forall(key => cart.get(key).fold(false)(countInCart => countInCart >= qualifyingCart.getOrElse(key, 0)))
  }

  /* returns flat rate savings if the cart is eligible, otherwise returns zero */
  def calculateSavings(cart: Cart): BigDecimal = {
    calculateSavings(cart, 0)
  }

  @tailrec
  private def calculateSavings(cart: Cart, savingsSoFar: BigDecimal): BigDecimal = {
    if (itemsDoQualifyForBundle(cart)) {
      calculateSavings(CheckoutCartPricingOperations.subtractCarts(cart, qualifyingCart), savingsSoFar + flatRateSavings)
    } else savingsSoFar
  }

}
