package axon.ml.dsl

object EvalDSL extends NumericDSL[Id] {

  override def const[N](n: N): Id[N] = n

  override def add[N](a: Id[N], b: Id[N])(implicit N: Numeric[N]): Id[N] =
    N.plus(a, b)

  override def subtract[N](a: Id[N], b: Id[N])(implicit N: Numeric[N]): Id[N] =
    N.minus(a, b)

  override def multiply[N](a: Id[N], b: Id[N])(implicit N: Numeric[N]): Id[N] =
    N.times(a, b)

  override def divide[N](a: Id[N], b: Id[N])(implicit N: Fractional[N]): Id[N] =
    N.div(a, b)
}
