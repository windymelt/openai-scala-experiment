val scala3Version = "3.2.2"

val http4sVersion = "0.23.18"

lazy val root = project
  .in(file("."))
  .settings(
    name := "openapi-scala-experiment",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.2.10",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-client" % "1.2.10",
      "org.http4s" %% "http4s-ember-client" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion
    ),
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
