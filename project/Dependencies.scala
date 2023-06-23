import sbt.*

object Dependencies {
  object Versions {
    lazy val skunk = "0.6.0"
    lazy val cats = "2.9.0"
    lazy val `cats-effect` = "3.4.8"
    lazy val weaver = "0.8.1"
    lazy val flyway = "9.16.0"
    lazy val enumeratum = "1.7.0"
    lazy val `test-container` = "1.17.6"
    lazy val postgresql = "42.5.4"
    lazy val logback = "1.4.7"
    lazy val log4cats = "2.5.0"
    lazy val refined = "0.10.2"
  }

  trait LibGroup {
    def all: Seq[ModuleID]
  }

  object ch {
    object qos {
      lazy val logback = "ch.qos.logback" % "logback-classic" % Versions.logback
    }
  }

  object eu {
    object timepit {
      object refined extends LibGroup {
        private def refined(artifact: String): ModuleID =
          "eu.timepit" %% artifact % Versions.refined

        lazy val core: ModuleID = refined("refined")
        lazy val cats: ModuleID = refined("refined-cats")
        lazy val pureconfig: ModuleID = refined("refined-pureconfig")
        override def all: Seq[ModuleID] = Seq(core, cats, pureconfig)
      }
    }
  }

  object org {
    object tpolecat {
      object Skunk extends LibGroup {
        private def skunk(artifact: String): ModuleID =
          "org.tpolecat" %% artifact % Versions.skunk

        lazy val core = skunk("skunk-core")
        lazy val circe = skunk("skunk-circe")
        override def all: Seq[ModuleID] = Seq(core, circe)
      }
    }

    object typelevel {
      object Cats extends LibGroup {
        lazy val core = "org.typelevel"   %% "cats-core"   % Versions.cats
        lazy val effect = "org.typelevel" %% "cats-effect" % Versions.`cats-effect`
        def all: Seq[ModuleID] = Seq(core, effect)
      }
      lazy val log4cats = "org.typelevel" %% "log4cats-slf4j" % Versions.log4cats
    }

    object testcontainers {
      lazy val postgresql = "org.testcontainers" % "postgresql" % Versions.`test-container`
    }

    object postgresql {
      lazy val core = "org.postgresql" % "postgresql" % Versions.postgresql
    }

    object flywaydb {
      val core = "org.flywaydb" % "flyway-core" % Versions.flyway
    }
  }

  object com {
    object disneystreaming {
      object Weaver extends LibGroup {
        private def weaver(artifact: String): ModuleID =
          "com.disneystreaming" %% s"weaver-$artifact" % Versions.weaver

        lazy val cats = weaver("cats")
        lazy val discipline = weaver("discipline")
        lazy val `scala-check` = weaver("scalacheck")
        override def all: Seq[ModuleID] =
          Seq(cats, discipline, `scala-check`)
      }
    }

    object beachape {
      object Enumeratum extends LibGroup {
        private def enumeratum(artifact: String): ModuleID =
          "com.beachape" %% artifact % Versions.enumeratum
        lazy val core = enumeratum("enumeratum")
        lazy val circe = enumeratum("enumeratum-circe")
        lazy val cats = enumeratum("enumeratum-cats")
        override def all: Seq[ModuleID] = Seq(core, circe, cats)
      }
    }
  }
}
