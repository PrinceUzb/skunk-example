package example.migration

import java.sql.DriverManager

import cats.effect.Sync
import cats.syntax.all._
import eu.timepit.refined.auto.autoUnwrap
import org.flywaydb.core.Flyway
import org.typelevel.log4cats.Logger
trait Migrations[F[_]] {
  /** Runs a migration */
  def migrate: F[Unit]
}

object Migrations {
  def make[F[_]: Sync](
      config: MigrationsConfig
    )(implicit
      logger: Logger[F]
    ): Migrations[F] = new Migrations[F] {
    def migrate: F[Unit] = for {
      _ <- Sync[F].blocking {
        val conn = DriverManager.getConnection(config.rootUrl)
        val stmt = conn.createStatement()
        stmt.execute(s"CREATE SCHEMA IF NOT EXISTS ${config.schema}")
        stmt.closeOnCompletion()
      }
      _ <- logger.info(s"Created schema if it didnt exist: ${config.schema}")
      flyway <- Sync[F].delay {
        Flyway
          .configure()
          .dataSource(config.url, config.username, config.password)
          .locations(config.location)
          .schemas(config.schema)
          .baselineOnMigrate(true)
          .table("flyway_history")
          .load()
      }
      _ <- Sync[F]
        .blocking(flyway.migrate())
        .void
        .handleErrorWith(err => logger.error(err)("Error occurred while migrating"))
    } yield {}
  }
}
