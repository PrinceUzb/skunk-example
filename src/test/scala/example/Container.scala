package example

import java.time.ZoneId

import cats.effect.IO
import cats.effect.Resource
import example.migration.Migrations
import example.migration.MigrationsConfig
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import weaver.scalacheck.CheckConfig
trait Container {
  def schemaName: String
  def migrationLocation: Option[String] = None

  type Res
  lazy val imageName: String = "postgres:12"
  lazy val container: PostgreSQLContainer[Nothing] = new PostgreSQLContainer(
    DockerImageName
      .parse(imageName)
      .asCompatibleSubstituteFor("postgres")
  )

  val customCheckConfig: CheckConfig = CheckConfig.default.copy(minimumSuccessful = 20)

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  val dbResource: Resource[IO, PostgreSQLContainer[Nothing]] =
    for {
      container <- Resource.fromAutoCloseable {
        IO {
          container.setCommand("postgres", "-c", "max_connections=150")
          container.addEnv("TZ", ZoneId.systemDefault().getId)
          container.start()
          container
        }
      }
      _ <- Resource.eval(logger.info("Container has started"))
      migrationConfig = MigrationsConfig(
        host = container.getHost,
        port = container.getFirstMappedPort,
        database = container.getDatabaseName,
        username = container.getUsername,
        password = container.getPassword,
        schema = schemaName,
        location = migrationLocation.getOrElse("db/migration"),
      )
      _ <- Resource.eval(logger.info(s"Migrating database at ${container.getJdbcUrl}"))
      _ <- Resource.eval(Migrations.make[IO](migrationConfig).migrate)
    } yield container
}
