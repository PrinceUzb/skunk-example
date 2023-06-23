package example.repositories

import eu.timepit.refined.types.string.NonEmptyString
import example.commonSyntaxAutoRefineV
import example.domain.UserRole
import skunk.Codec
import skunk.codec.all._
import skunk.data.Type

package object sql {
  val nes: Codec[NonEmptyString] = varchar.imap[NonEmptyString](identity(_))(_.value)
  val role: Codec[UserRole] = `enum`[UserRole](UserRole, Type("user_role"))
}
