package sammons.checkoutcartpricing

import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable._

class FlatRateBundleTest extends Specification with Mockito {

  trait Setup extends Scope {

    val qualifyingCart = Map[CatalogItem, Int](CatalogItem("1","apple", 1) -> 5)
    val customBundle = new FlatRateBundle(qualifyingCart = qualifyingCart, 0.2)
  }

  "itemsDoQualifyForBundle" should {
    "return whether or not there are enough items of each type needed to qualify" in new Setup {
      val cartWithEnoughApples = Map[CatalogItem, Int](CatalogItem("1","apple", 1) -> 10)
      val cartWithoutEnough = Map[CatalogItem, Int](CatalogItem("1","apple", 1) -> 4)
      val cartWithExactlyEnough = Map[CatalogItem, Int](CatalogItem("1","apple", 1) -> 5)

      customBundle.itemsDoQualifyForBundle(cartWithEnoughApples) must beTrue
      customBundle.itemsDoQualifyForBundle(cartWithoutEnough) must beFalse
      customBundle.itemsDoQualifyForBundle(cartWithExactlyEnough) must beTrue
    }

  }

  "calculateSavings" should {
    "calculate the correct amount" in new Setup {
      val cartWithEnoughApplesForThreeApplications = Map[CatalogItem, Int](CatalogItem("1","apple", 1) -> 16)

      customBundle.calculateSavings(cartWithEnoughApplesForThreeApplications) must_=== 0.6
    }
  }

}