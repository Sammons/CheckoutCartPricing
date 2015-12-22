package sammons.checkoutcartpricing.bundlerulesets

import sammons.checkoutcartpricing.CartType.Cart
import sammons.checkoutcartpricing.{CatalogItem, CheckoutBundle}

import scala.annotation.tailrec

/* like saying you save 1 cent more on every apple, for every extra apple purchased, up to a max of 5 cents per apple */
class BuyAtIncreasingDiscountForUpToMaxSavingsPerItemRuleSet(ruleSetId: String, item: CatalogItem, discountMultiplier: BigDecimal, maxSavingsPerItem: BigDecimal) extends BundleRuleSet {
  val id = ruleSetId

  override def calculatePossibleBundles(cart: Cart): Set[CheckoutBundle] = {
    getBundleForEveryPossibleCountOfItems(item, cart.getOrElse(item, 0), Set())
  }

  @tailrec
  private def getBundleForEveryPossibleCountOfItems(item: CatalogItem, count: Int, bundlesSoFar: Set[CheckoutBundle]): Set[CheckoutBundle] = {
    if (count == 0) return bundlesSoFar
    val savingsPerApple = count * discountMultiplier
    val netSavings = if (savingsPerApple <= maxSavingsPerItem) savingsPerApple * count else maxSavingsPerItem * count
    getBundleForEveryPossibleCountOfItems(item, count - 1, Set(CheckoutBundle(Map[CatalogItem, Int](item -> count), netSavings, ruleSetId)) ++ bundlesSoFar)
  }
}
