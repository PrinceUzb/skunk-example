ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "skunk-example",
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    libraryDependencies ++=
      Dependencies.org.typelevel.Cats.all ++
        Dependencies.org.tpolecat.Skunk.all ++
        Dependencies.com.disneystreaming.Weaver.all.map(_ % Test) ++
        Dependencies.com.beachape.Enumeratum.all ++
        Dependencies.eu.timepit.refined.all ++
        Seq(
          Dependencies.org.flywaydb.core,
          Dependencies.org.typelevel.log4cats,
          Dependencies.ch.qos.logback,
          Dependencies.org.postgresql.core           % Test,
          Dependencies.org.testcontainers.postgresql % Test,
        ),
  )
