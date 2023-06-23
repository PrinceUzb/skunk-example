package example

import java.util.UUID

import example.commonSyntaxAutoRefineV
import example.domain.User
import example.domain.UserRole
import example.repositories.UsersRepo
object UsersRepoSpec extends DBSuite {
  override def schemaName: String = "example"

  test("Create User") { implicit postgres =>
    val user = User(UUID.randomUUID(), "John Dao", UserRole.Admin)
    UsersRepo
      .make[F]
      .create(user)
      .as(success)
      .handleErrorWith { error =>
        logger
          .error(error)("Error: ")
          .as(
            failure("Test failed.")
          )
      }
  }
}
