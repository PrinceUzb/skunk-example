package example.repositories.sql

import example.domain.User
import skunk._
import skunk.codec.all.uuid
import skunk.implicits._

private[repositories] object UsersSql {
  private val columns = uuid *: nes *: role
  private val codec: Codec[User] = columns.to[User]
  val insert: Command[User] =
    sql"""INSERT INTO example.users VALUES ($codec)""".command
}
