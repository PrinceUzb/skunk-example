package example.migration

case class MigrationsConfig(
    host: String,
    port: Int,
    username: String,
    password: String,
    database: String,
    schema: String,
    location: String,
  ) {
  lazy val rootUrl: String =
    s"jdbc:postgresql://$host:$port/$database?user=$username&password=$password"

  lazy val url: String =
    s"jdbc:postgresql://$host:$port/$database"
}
