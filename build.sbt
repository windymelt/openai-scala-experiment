val scala3Version = "3.2.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "openapi-scala-experiment",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.2.10"
    ),
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
