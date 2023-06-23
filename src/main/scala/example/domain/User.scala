package example.domain

import java.util.UUID

import eu.timepit.refined.types.string.NonEmptyString

case class User(
    id: UUID,
    name: NonEmptyString,
    role: UserRole,
  )
