import Dependencies._

ThisBuild / scalaVersion := "2.13.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "simple-kafka-example",
    resolvers ++= Seq(
      "confluent-release" at "http://packages.confluent.io/maven/"
    ),
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.apache.kafka" %% "kafka" % "2.6.0",
      "io.confluent" % "kafka-avro-serializer" % "3.3.1"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
