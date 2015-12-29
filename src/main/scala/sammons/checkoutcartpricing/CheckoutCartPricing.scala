package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CheckoutCartPricingExceptions.{ItemsDoNotExistInCatalog, EmptyCatalogException}
import sammons.checkoutcartpricing.bundlerulesets.BundleRuleSet

import scala.collection.mutable


class CheckoutCartPricing(catalogItems: Seq[CatalogItem], possibleBundles: Seq[BundleRuleSet]) {
  import CartType._

  /* Given a cart (Map[CatalogItem, Int]) calculates a CheckoutCart, which contains
   * the lowest total possible, and a sequence of the best bundles to use.
   */
  def calculateCheapestCheckoutCart(cart: Cart): CheckoutCart = {
    if (catalogItems.isEmpty)
      throw EmptyCatalogException()

    val catalogAsSet = catalogItems.toSet
    val itemsNotInCatalog = cart.keys.filter(!catalogAsSet(_))
    if (itemsNotInCatalog.nonEmpty)
      throw ItemsDoNotExistInCatalog(itemsNotInCatalog.toSeq)
    
    val bundleCombinations = mutable.Map[Cart, Seq[CheckoutBundle]]()

    observeBundleCombinations(allEligibleBundlesFor(cart), cart, Map(), Seq(), bundleCombinations)
    val bundlesGroupedBySum = bundleCombinations.values.groupBy(b => b.map(_.savings).sum)
    val bestSavingsWithFewestBundles = bundlesGroupedBySum(bundlesGroupedBySum.keys.max).minBy(_.size)
    val netPrice = cart.price - bestSavingsWithFewestBundles.map(_.savings).sum

    CheckoutCart(cart, bestSavingsWithFewestBundles, if (netPrice >= 0) netPrice else 0)
  }

  /* combines every combination of eligible bundles with the others, whenever a set of
   * bundles creates a cart that overlaps with another set of bundles, only the better-saving-seq of bundles
   * is used to proceed */
  private def observeBundleCombinations(allPossibleBundles: Seq[CheckoutBundle],
                                    previousCart: Cart,
                                    cartConstructSoFar: Cart,
                                    bundleSeqConstructSoFar: Seq[CheckoutBundle],
                                    observedCarts: mutable.Map[Cart, Seq[CheckoutBundle]]): Unit = {
    if (allPossibleBundles.nonEmpty && previousCart.nonEmpty) {
      /* while there are bundles to try */
      allPossibleBundles.foreach { bundle =>
        /* make new constructs, building off of the existing values */
        val currentBundleCartConstruct = cartConstructSoFar plus bundle.itemsInBundle
        val currentBundleSeq = bundleSeqConstructSoFar ++ Seq(bundle)
        val previouslyObservedBundleSeq = observedCarts.getOrElse(currentBundleCartConstruct, Seq())
        val previouslyObservedSavings = previouslyObservedBundleSeq.map(_.savings).sum
        val currentSavings = currentBundleSeq.map(_.savings).sum
        /* if better savings have been observed with this cart then halt, otherwise
         * continue by removing items in bundle from the previousCart,
         * updating the cache, and then recursing. prioritize smaller bundle sequences.
         */
        if (currentSavings > previouslyObservedSavings ||
          (currentSavings == previouslyObservedSavings && previouslyObservedBundleSeq.size > currentBundleSeq.size)) {
          observedCarts(currentBundleCartConstruct) = currentBundleSeq
          val currentCart = previousCart minus bundle.itemsInBundle
          val bundlesThatWorkWithCurrentCart = allPossibleBundles.filter(_.itemsInBundle belongTo currentCart)
          observeBundleCombinations(bundlesThatWorkWithCurrentCart, currentCart, currentBundleCartConstruct, currentBundleSeq, observedCarts)
        }
      }
    }
  }

  private def allEligibleBundlesFor(cart: Cart): Seq[CheckoutBundle] = {
    this.possibleBundles.flatMap(_.calculatePossibleBundles(cart))
  }
}