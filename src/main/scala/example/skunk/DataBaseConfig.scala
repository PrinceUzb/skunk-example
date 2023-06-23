package example.skunk

import eu.timepit.refined.types.net.NonSystemPortNumber
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

case class DataBaseConfig(
    host: NonEmptyString,
    port: NonSystemPortNumber,
    user: NonEmptyString,
    password: NonEmptyString,
    database: NonEmptyString,
    poolSize: PosInt,
  )
