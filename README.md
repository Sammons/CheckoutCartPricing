## CheckoutCartPricing

This is a toy scala library that calculates the minimum cost, given a shopping cart, a catalog, and a set of bundles available.

There are CatalogItems, Bundles, and a CheckoutCartPricingImpl object that exposes the logic through two endpoints:

1. `def calculateCheckoutCartPrice(checkoutCart: Cart): BigDecimal`

2. `def initializeCheckoutCartPriceCalculationSystem(catalogItems: Seq[CatalogItem], bundles: Seq[Bundle]): Unit`

* A `Cart` is a `Map[CatalogItem, Int]` and a `CatalogItem` is a `case class CatalogItem(id: String, name: String, value: BigDecimal)`

## Caveats / Notes

* currency is not managed explicitly, everything is assumed to be the same currency and BigDecimal is used.

* bundles stack. They do not exclude eachother

* the initialization of the catalog and bundles should be safe, but no testing around the threadsafeness has been implemented.

## Testing

from the command line run `sbt test`

## Pulling into another project

* There is a sample project at https://github.com/sammons/CheckoutCartPricing-Example.

here is a demo build.scala that can automatically clone down this code into a project alongside another. The library has not been published to any public repositories. 

```
import sbt._
import Keys._

object SampleBuild extends Build {
  lazy val root: Project = Project(
    id = "sample",
    base = file("."),
    settings = Seq(
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.11.7",
      resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases", /* for specs2 */
      scalacOptions := Seq("-Yrangepos"/* for specs2 */),
      libraryDependencies := Seq(
        "org.scala-lang" % "scala-compiler" % scalaVersion.value,
        "org.scala-lang" % "scala-library" % scalaVersion.value,
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "org.specs2" %% "specs2-core" % "3.0" % "test",
        "org.specs2" %% "specs2-mock" % "3.0" % "test"
     )
    )
  ).dependsOn(uri("https://github.com/Sammons/CheckoutCartPricing.git"))
}
```