package axon.ml.dsl

class ThunkDSL extends NumericDSL[Thunk] {

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

object ThunkDSL {

  implicit val thunkDSL: ThunkDSL = new ThunkDSL

  def liftThunk[N](data: Vector[Vector[N]]): Vector[Vector[Thunk[N]]] =
    data.map(_.map(n => () => n))

  def execute[N](thunks: Vector[Vector[Thunk[N]]]): Vector[Vector[N]] =
    thunks.map(v => v.map(n => n()))
}
