package sammons.checkoutcartpricing

object CartType {
  type Cart = Map[CatalogItem, Int]

  implicit class SubtractionCalculation(leftCart: Cart) {
    def minus(cart: Cart): Cart = {
      leftCart.map {
        case (item: CatalogItem, cartACount: Int) =>
          cart.get(item).fold( (item, cartACount) )(cartBCount => (item, cartACount - cartBCount))
      }.filter(tuple => tuple._2 > 0)
    }
  }

  implicit class CartSubsetCalculation(subsetCart: Cart) {
    def belongTo(cart: Cart): Boolean = {
      subsetCart.forall({case (item, count) => cart.getOrElse(item, 0) >= count})
    }
  }

  implicit class CartCostCalculation(cart: Cart) {
    def price: BigDecimal = {
      cart.map({case (k,count) => k.value * count}).sum
    }
  }

  implicit class NumberOfItemsCalculation(cart: Cart) {
    def numberOfItems: Int = cart.values.sum
  }

  implicit class UnionCalculation(leftCart: Cart) {
    def plus(cart: Cart): Cart = {
      (leftCart.keySet ++ cart.keySet).map { k =>
        (k, cart.getOrElse(k, 0) + leftCart.getOrElse(k, 0))
      }.toMap
    }
  }
}
