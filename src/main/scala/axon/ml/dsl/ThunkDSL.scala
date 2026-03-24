package axon.ml.dsl

object ThunkDSL extends NumericDSL[Thunk] {

  override def const[N](n: N): Thunk[N] = () => n

  override def add[N](a: Thunk[N], b: Thunk[N])(implicit N: Numeric[N]): Thunk[N] =
    () => N.plus(a(), b())

  override def subtract[N](a: Thunk[N], b: Thunk[N])(implicit N: Numeric[N]): Thunk[N] =
    () => N.minus(a(), b())

  override def multiply[N](a: Thunk[N], b: Thunk[N])(implicit N: Numeric[N]): Thunk[N] =
    () => N.times(a(), b())

  override def divide[N](a: Thunk[N], b: Thunk[N])(implicit N: Fractional[N]): Thunk[N] =
    () => N.div(a(), b())
}
