import cats.syntax.all._
import eu.timepit.refined.api.RefType
import eu.timepit.refined.api.Refined
import eu.timepit.refined.api.Validate

package object example {
  implicit def commonSyntaxAutoRefineV[T, P](
      unrefined: T
    )(implicit
      validate: Validate[T, P],
      refType: RefType[Refined],
    ): Refined[T, P] =
    refType.refine[P](unrefined).valueOr(err => throw new IllegalArgumentException(err))
}
