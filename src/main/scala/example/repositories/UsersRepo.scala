package example.repositories

import cats.effect.MonadCancel
import cats.effect.Resource
import example.domain.User
import example.repositories.sql.UsersSql
import example.skunk.all.skunkSyntaxCommandOps
import skunk.Session

trait UsersRepo[F[_]] {
  def create(user: User): F[Unit]
}

object UsersRepo {
  def make[F[_]](
      implicit
      session: Resource[F, Session[F]],
      F: MonadCancel[F, Throwable],
    ): UsersRepo[F] = new UsersRepo[F] {
    override def create(user: User): F[Unit] =
      UsersSql.insert.execute(user)
  }
}
