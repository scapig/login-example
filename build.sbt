import _root_.play.sbt.PlayImport._

import sbt.Keys._
import sbt._

name := "login-example"

version := "1.0"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq( ws, guice )

libraryDependencies += "com.mohiva" %% "play-silhouette" % "5.0.2"
libraryDependencies += "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.2"
libraryDependencies += "com.mohiva" %% "play-silhouette-persistence" % "5.0.2"
libraryDependencies += "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.2"
libraryDependencies += "com.iheart" %% "ficus" % "1.4.1"

libraryDependencies += "org.webjars" %% "webjars-play" % "2.6.1"
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery")
libraryDependencies += "org.webjars" % "jquery" % "3.2.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % "test"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % "test"
libraryDependencies += "com.github.tomakehurst" % "wiremock-standalone" % "2.8.0" % "it, component"
libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0" % "it, component"
libraryDependencies += "com.mohiva" %% "play-silhouette-testkit" % "5.0.2" % "test"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

lazy val microservice = (project in file("."))
  .enablePlugins(Seq(play.sbt.PlayScala) : _*)
  .configs(IntTest)
  .settings(inConfig(IntTest)(Defaults.testSettings): _*)
  .settings(
    Keys.fork in IntTest := false,
    unmanagedSourceDirectories in IntTest <<= (baseDirectory in IntTest) (base => Seq(base / "it"))
  )
  .configs(ComponentTest)
  .settings(inConfig(ComponentTest)(Defaults.testSettings): _*)
  .settings(
    Keys.fork in ComponentTest := false,
    unmanagedSourceDirectories in ComponentTest <<= (baseDirectory in ComponentTest) (base => Seq(base / "component"))
  )

lazy val IntTest = config("it") extend Test
lazy val ComponentTest = config("component") extend Test
