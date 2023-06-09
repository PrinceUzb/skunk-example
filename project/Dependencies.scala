import sbt.*

object Versions {
  lazy val skunk = "0.2.3"
  lazy val cats = "2.9.0"
  lazy val `cats-effect` = "3.4.8"
  lazy val weaver = "0.8.1"
  lazy val flyway = "9.16.0"
  lazy val enumeratum = "1.7.0"
  lazy val `test-container` = "1.17.6"
  lazy val postgresql = "42.5.4"
}

object Dependencies {
  trait LibGroup {
    def all: Seq[ModuleID]
  }

  object org {
    object tpolecat {
      object Skunk extends LibGroup {
        private def skunk(artifact: String): ModuleID =
          "org.tpolecat" %% artifact % Versions.skunk

        lazy val core = skunk("skunk-core")
        lazy val circe = skunk("skunk-circe")
        lazy val refined = skunk("refined")
        override def all: Seq[ModuleID] = Seq(core, circe, refined)
      }
    }

    object typelevel {
      object Cats extends LibGroup {
        lazy val core = "org.typelevel"   %% "cats-core"   % Versions.cats
        lazy val effect = "org.typelevel" %% "cats-effect" % Versions.`cats-effect`
        def all: Seq[ModuleID] = Seq(core, effect)
      }
    }

    object testcontainers {
      lazy val `test-container` = "org.testcontainers" % "postgresql" % Versions.`test-container`
    }

    object postgresql {
      lazy val postgresql = "org.postgresql" % "postgresql" % Versions.postgresql
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

        lazy val `weaver-cats` = weaver("cats")
        lazy val `weaver-discipline` = weaver("discipline")
        lazy val `weaver-scala-check` = weaver("scalacheck")
        override def all: Seq[ModuleID] =
          Seq(`weaver-cats`, `weaver-discipline`, `weaver-scala-check`)
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
