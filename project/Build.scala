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
    "joda-time"               % "joda-time"            % "2.1",
    "org.joda"                % "joda-convert"         % "1.2",
    "mysql"                   % "mysql-connector-java" % "5.1.6",
    "ch.qos.logback"          % "logback-classic"      % "1.1.2",
    "org.mockito"             % "mockito-core"         % "1.9.5" % "test",
    "commons-io"              % "commons-io"           % "2.4",
    "org.specs2"              %% "specs2-core"         % "2.4.13" % "test",
    "org.apache.commons"      % "commons-email"        % "1.3.2",
    "org.apache.commons"      % "commons-compress"     % "1.6",
    "com.github.scopt"        %% "scopt"               % "3.2.0",
    "com.github.mauricio"     %% "postgresql-async"    % "0.2.+",
    "com.github.mauricio"     %% "mysql-async"         % "0.2.+",
    "org.slf4j"               %  "slf4j-simple"        % "1.7.+",
    "com.typesafe"            % "config"               % "1.2.1",
    "org.jsoup"               % "jsoup"                % "1.8.1",
    "org.scalaj"              %% "scalaj-http"         % "0.3.16",
    "org.scalikejdbc"         %% "scalikejdbc"         % "2.2.0"
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
  ).aggregate(core, stockPriceComplementer)

  lazy val core = Project(
    id = "core",
    base = file("module/core"),
    settings = Defaults.defaultSettings ++ Seq(
      resolvers ++= all,
      libraryDependencies ++= dependencies
    )
  )

  lazy val stockPriceComplementer = Project(
    id = "stock_price_complementer",
    base = file("module/stock_price_complementer"),
    settings = Defaults.defaultSettings ++ Seq(
      resolvers ++= all,
      libraryDependencies ++= dependencies
    )
  ).dependsOn(core)

  lazy val codeTest = Project(
    id = "code_test",
    base = file("module/code_test"),
    settings = Defaults.defaultSettings ++ Seq(
      resolvers ++= all,
      libraryDependencies ++= dependencies
    )
  ).dependsOn(core)
}