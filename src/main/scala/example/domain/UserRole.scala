package example.domain

import scala.collection.immutable

import enumeratum.EnumEntry.Snakecase
import enumeratum._

sealed trait UserRole extends EnumEntry with Snakecase
object UserRole extends Enum[UserRole] with CirceEnum[UserRole] {
  final case object Editor extends UserRole
  final case object Owner extends UserRole
  final case object Admin extends UserRole
  override def values: immutable.IndexedSeq[UserRole] = findValues
}
