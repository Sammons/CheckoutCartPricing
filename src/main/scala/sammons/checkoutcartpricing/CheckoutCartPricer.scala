package sammons.checkoutcartpricing

import java.math.BigDecimal

trait CheckoutCartPricer {
  def price(cart: Seq[CartEntry]): BigDecimal

}
