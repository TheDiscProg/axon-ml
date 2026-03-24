import sbt.librarymanagement.CrossVersion
import sbt.url
import xerial.sbt.Sonatype._


lazy val scala2 = "2.13.18"
lazy val scala38 = "3.8.2"
lazy val supportedScalaVersions = List(scala2, scala38)

lazy val commonSettings = Seq(
  scalaVersion := scala38,
  libraryDependencies ++= Dependencies.all
)

lazy val root = (project in file("."))
  .enablePlugins(
    ScalafmtPlugin
  )
  .settings(
    commonSettings,
    name := "axon-ml",
    scalacOptions ++= Scalac.options,
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2,13)) => Seq("-Ytasty-reader")
        case _ => Seq("-Yretain-trees")
      }
    },
    crossScalaVersions := supportedScalaVersions
  )

ThisBuild / version := "0.0.1"
ThisBuild / organization := "io.github.thediscprog"
ThisBuild / organizationName := "thediscprog"
ThisBuild / organizationHomepage := Some(url("https://github.com/TheDiscProg"))

ThisBuild / description := "Axon ML - A Scala library for building machine learning models"

// Sonatype/Maven Publishing
ThisBuild / publishMavenStyle := true
ThisBuild / sonatypeCredentialHost := sonatypeCentralHost
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeProfileName := "io.github.thediscprog"
ThisBuild / licenses := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/TheDiscProg/simex-messaging"))
ThisBuild / sonatypeProjectHosting := Some(GitHubHosting("TheDiscProg", "axon-ml", "TheDiscProg@gmail.com"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/TheDiscProg/axon-ml"),
    "scm:git@github.com:thediscprog/axon-ml.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "thediscprog",
    name = "TheDiscProg",
    email = "TheDiscProg@gmail.com",
    url = url("https://github.com/TheDiscProg")
  )
)

usePgpKeyHex("FC6901A47E5DA2533DCF25D51615DCC33B57B2BF")

sonatypeCredentialHost := "central.sonatype.com"
sonatypeRepository := "https://central.sonatype.com/api/v1/publisher/"

ThisBuild / versionScheme := Some("early-semver")

addCommandAlias("cleanTest", ";clean;scalafmt;test:scalafmt;test;")
addCommandAlias("cleanCoverage", ";clean;scalafmt;test:scalafmt;coverage;test;coverageReport;")
