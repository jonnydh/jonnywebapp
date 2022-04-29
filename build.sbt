name := """jonnyWebApp"""
organization := "personal"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

resolvers += "HMRC-open-artefacts-maven2" at "https://open.artefacts.tax.service.gov.uk/maven2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies ++= Seq(
  "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28" % "0.63.0"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "personal.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "personal.binders._"
