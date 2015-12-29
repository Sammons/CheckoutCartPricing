package sammons.checkoutcartpricing

object CheckoutCartPricingExceptions {
  case class EmptyCatalogException() extends Throwable
  case class ItemsDoNotExistInCatalog(catalogItems: Seq[CatalogItem]) extends Throwable
}
