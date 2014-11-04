import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    name := "stcok_trader",
    version := "1.0",
    scalaVersion := "2.10.4"
  )
}

object Dependencies {
  val dependencies = Seq(
    "org.specs2" %% "specs2-core" % "2.4.9" % "test",
    "joda-time" % "joda-time" % "2.1",
    "org.joda" % "joda-convert" % "1.2",
    "mysql" % "mysql-connector-java" % "5.1.6",
    "org.scalikejdbc" %% "scalikejdbc" % "2.1.2",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.mockito" % "mockito-core" % "1.9.5" % "test",
    "org.apache.commons" % "commons-compress" % "1.6",
    "commons-io" % "commons-io" % "2.4",
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "org.apache.commons" % "commons-email" % "1.3.2",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
    "com.github.scopt" %% "scopt" % "3.2.0",
    "com.typesafe.play" %% "play-json" % "2.3.4"
  )
}

object Resolvers {
  val all = Seq(
    "Typesafe Simple Repository" at "http://repo.typesafe.com/typesafe/simple/maven-releases/"
  )
}

object ApplicationBuild extends Build {
  import BuildSettings._
  import Dependencies._
  import Resolvers._

  lazy val root = Project(
    id = "stock_trader",
    base = file("."),
    settings = buildSettings ++ Seq(
      resolvers ++= all,
      libraryDependencies ++= dependencies
    )
  )

  lazy val core = Project(
    id = "core",
    base = file("module/core")
  ).dependsOn(root)

  lazy val stockPriceComplementer = Project(
    id = "stock_price_complementer",
    base = file("module/stock_price_complementer")
  ).dependsOn(root, core)
}