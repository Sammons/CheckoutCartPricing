package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CheckoutCartPricingTypes._

object CheckoutCartPricingTypes {
  type Cart = Map[CatalogItem, Int]
}

object CheckoutCartPricingOperations {

  /* subtracts counts of maps in cart B from counts in A.
   * if A does not not contain a key, then does nothing for that key.
   * if a count goes below zero, then the the key is removed from the map. */
  def subtractCarts(cartA: Cart, cartB: Cart): Cart = {
    cartA.map {
      case (item: CatalogItem, cartACount: Int) =>
        cartB.get(item).fold( (item, cartACount) )(cartBCount => (item, cartACount - cartBCount))
    }.filter(tuple => tuple._2 > 0)
  }
}
