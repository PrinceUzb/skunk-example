package example

import _root_.skunk._
import _root_.skunk.codec.all.text
import _root_.skunk.implicits._
import _root_.skunk.util.Typer
import cats.effect.IO
import cats.effect.Resource
import natchez.Trace.Implicits.noop
import weaver.IOSuite
import weaver.scalacheck.CheckConfig
import weaver.scalacheck.Checkers

trait DBSuite extends IOSuite with Checkers with Container {
  type Res = Resource[IO, Session[IO]]

  def beforeAll(implicit session: Resource[IO, Session[IO]]): IO[Unit] = IO.unit

  def checkPostgresConnection(
      postgres: Resource[IO, Session[IO]]
    ): IO[Unit] =
    postgres.use { session =>
      session.unique(sql"select version();".query(text)).flatMap { v =>
        logger.info(s"Connected to Postgres $v")
      }
    }

  override def sharedResource: Resource[IO, Res] =
    for {
      container <- dbResource
      session <- Session
        .pooled[IO](
          host = container.getHost,
          port = container.getFirstMappedPort,
          user = container.getUsername,
          password = Some(container.getPassword),
          database = container.getDatabaseName,
          max = 100,
          strategy = Typer.Strategy.SearchPath,
          parameters = Map("search_path" -> schemaName) ++ Session.DefaultConnectionParameters,
        )
        .evalTap(checkPostgresConnection)
      _ <- Resource.eval(beforeAll(session))
    } yield session
}
