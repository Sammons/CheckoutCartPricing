package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CheckoutCartPricingExceptions.EmptyCatalogException

trait CheckoutCartPricing {
  /* Takes a map of catalog item ID -> Count of items in the user's cart. Returns a bigdecimal result
   * of the smallest total available by applying all bundles that the cart qualifies for (items may participate
   * in multiple bundles - e.g. "buy 2 apples get the rest half off", and "buy 2 apples get a free snicker" could
   * both be applied to a cart with only 3 apples.). Ensures that the resulting value is always greater than or
   * equal to zero.
   *
   * Note about currencies: logic handling the catalogItems and bundles assumes the currency is the same between
   * initialization and this method; there are no constructs to manage what currency is being used, and no currency
   * conversions will be performed.*/
  def calculateCheckoutCartPrice(checkoutCart: Map[CatalogItem, Int]): BigDecimal

  /* Updates the catalog and bundles available. Calling this method again will overwrite the
   * existing values. This operation should be threadsafe; it synchronizes on the CheckoutCartPricing object.
   */
  def initializeCheckoutCartPriceCalculationSystem(catalogItems: Seq[CatalogItem], bundles: Seq[Bundle]): Unit
}

object CheckoutCartPricingImpl extends CheckoutCartPricing {

  private var catalogItems: Seq[CatalogItem] = Seq()
  private var bundles: Seq[Bundle] = Seq()

  def initializeCheckoutCartPriceCalculationSystem(catalogItems: Seq[CatalogItem], bundles: Seq[Bundle]): Unit = this.synchronized {
    this.catalogItems = catalogItems
    this.bundles = bundles
  }

  def calculateCheckoutCartPrice(checkoutCart: Map[CatalogItem, Int]): BigDecimal = {
    if (catalogItems.isEmpty) throw EmptyCatalogException()
    val savings: BigDecimal =
      bundles.filter(b => b.itemsDoQualifyForBundle(checkoutCart)).map(_.calculateSavings(checkoutCart)).sum
    val costWithNoSavings = checkoutCart.map({case (key: CatalogItem, count: Int) => key.value * count}).sum
    val revisedCost = costWithNoSavings - savings
    if (revisedCost > 0) revisedCost else 0
  }

}