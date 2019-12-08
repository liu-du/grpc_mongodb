import Dependencies._
import scalapb.compiler.Version.scalapbVersion

ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.liudu"
ThisBuild / organizationName := "liudu"

lazy val root = (project in file("."))
  .settings(
    name := "blog",
    libraryDependencies ++= Seq(
      // scalapb
      "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
      // grpc
      "io.grpc" % "grpc-netty-shaded" % scalapb.compiler.Version.grpcJavaVersion, // include SSL libraries
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
      // mongodb
      "org.mongodb" % "mongodb-driver-sync" % "3.11.2",
      // json
      "org.json4s" %% "json4s-native" % "3.6.7",
      // scalatest
      scalaTest % Test
    )
  )

PB.targets in Compile := Seq(
  scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
)

fork := true
outputStrategy := Some(StdoutOutput)
