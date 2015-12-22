package sammons.checkoutcartpricing

import sammons.checkoutcartpricing.CartType.Cart


case class CheckoutCart(cart: Cart, applicableBundles: Seq[CheckoutBundle], total: BigDecimal)
