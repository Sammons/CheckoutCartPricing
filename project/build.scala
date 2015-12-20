import sbt.Build
import sbt._
import Keys._

object CheckoutCartPricingBuild extends Build {

  /*
  configured based on
  https://github.com/scalamacros/sbt-example/blob/master/project/Build.scala
  and sections in http://www.scala-sbt.org/release/tutorial/index.html
   */
  lazy val root: Project = Project(
    id = "checkout-cart-pricing",
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
  )

}