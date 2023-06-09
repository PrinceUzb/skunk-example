package example

trait DBSuite extends IOSuite with Checkers with Container {
  type Res = Resource[IO, Session[IO]]

  def beforeAll(implicit session: Resource[IO, Session[IO]]): IO[Unit] = IO.unit

  override def checkConfig: CheckConfig = CheckConfig.default.copy(minimumSuccessful = 1)

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
        )
        .evalTap(checkPostgresConnection)
      _ <- Resource.eval(beforeAll(session))
    } yield session
}
