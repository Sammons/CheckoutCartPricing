package sammons.checkoutcartpricing

object CartType {
  type Cart = Map[CatalogItem, Int]

  def subtractCarts(cartA: Cart, cartB: Cart): Cart = {
    cartA.map {
      case (item: CatalogItem, cartACount: Int) =>
        cartB.get(item).fold( (item, cartACount) )(cartBCount => (item, cartACount - cartBCount))
    }.filter(tuple => tuple._2 > 0)
  }

  def cartIsSubsetOfOtherCart(cart: Cart, subsetCart: Cart): Boolean = {
    subsetCart.forall({case (item, count) => cart.getOrElse(item, 0) >= count})
  }

  implicit class CartSubsetCalculation(subsetCart: Cart) {
    def belongTo(cart: Cart): Boolean = {
      cartIsSubsetOfOtherCart(cart, subsetCart)
    }
  }

  implicit class CartCostCalculation(cart: Cart) {
    def price: BigDecimal = {
      cart.map({case (k,count) => k.value * count}).sum
    }
  }
}
