import java.util.Calendar

ThisBuild / scalaVersion := "3.3.0"
ThisBuild / version := "2.3.1"
ThisBuild / organization := "com.stulsoft"
ThisBuild / organizationName := "stulsoft"

lazy val root = (project in file("."))
  .settings(
    name := "ys-file-tools",
    maintainer := "ysden123@gmail.com",
    libraryDependencies += "com.stulsoft" %% "common" % "latest.integration",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.7",
    libraryDependencies += "commons-io" % "commons-io" % "2.13.0",
    libraryDependencies += "org.apache.commons" % "commons-imaging" % "1.0-alpha3",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",

    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
  )
  .enablePlugins(JavaAppPackaging)

Compile / packageBin / packageOptions += Package.ManifestAttributes("Build-Date" -> Calendar.getInstance().getTime.toString)

Compile / mainClass := Some("com.stulsoft.file.tools.Main")