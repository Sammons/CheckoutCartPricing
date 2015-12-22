package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CheckoutCartPricingExceptions.EmptyCatalogException
import sammons.checkoutcartpricing.bundlerulesets.BundleRuleSet


object CheckoutCartPricing {
  import CartType._

  private var catalogItems: Seq[CatalogItem] = Seq()
  private var possibleBundles: Seq[BundleRuleSet] = Seq()

  def initializeCheckoutCartPriceCalculationSystem(catalogItems: Seq[CatalogItem], possibleBundles: Seq[BundleRuleSet]): Unit = this.synchronized {
    this.catalogItems = catalogItems
    this.possibleBundles = possibleBundles
  }

  def calculateCheapestCheckoutCart(cart: Cart): CheckoutCart = {
    if (catalogItems.isEmpty)
      throw EmptyCatalogException()

    val catalogAsSet = this.catalogItems.toSet

    if (cart.keys.exists(item => !catalogAsSet(item)))
      throw new IllegalArgumentException("Item in cart does not exist in the catalog")

    /* for each bundle potential, try every other bundle potential minus the current cart */
    val bestBundles = calculateBestBundles(cart, cart, calculateAllPossibleBundles(cart))
    val netPrice = cart.price - bestBundles.map(_.savings).sum

    CheckoutCart(cart, bestBundles, if (netPrice >= 0) netPrice else 0)
  }

  private def calculateBestBundles(originalCart: Cart, cart: Cart, possibleBundles: Seq[CheckoutBundle], bundlesSoFar: Seq[CheckoutBundle] = Seq()): Seq[CheckoutBundle] = {
    val possibleBundleCombinations = possibleBundles
      .filter(_.itemsInBundle belongTo cart)
      .map({ bundle =>
        val cartWithoutChosenBundle = CartType.subtractCarts(cart, bundle.itemsInBundle)
        calculateBestBundles(originalCart, cartWithoutChosenBundle, possibleBundles, bundlesSoFar ++ Seq(bundle))
      })
    possibleBundleCombinations.maxBy(_.map(_.savings).sum)
  }

  private def calculateAllPossibleBundles(cart: Cart): Seq[CheckoutBundle] = {
    this.possibleBundles.flatMap(_.calculatePossibleBundles(cart))
  }
}