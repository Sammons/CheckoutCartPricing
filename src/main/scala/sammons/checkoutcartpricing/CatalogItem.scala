package sammons.checkoutcartpricing

import java.math.BigDecimal

trait CatalogItem {
  def name: String
  def value: BigDecimal
}
