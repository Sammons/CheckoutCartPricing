package sammons.checkoutcartpricing

import org.specs2.matcher.Scope
import org.specs2.mutable._

class RawPricer extends Specification {

  trait Setup extends Scope {
    val a = "test"
  }

  "whatever" should {
    "do something special" in new Setup {
      a shouldEqual "othertest"
    }
  }

}
