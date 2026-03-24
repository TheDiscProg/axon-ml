package axon.ml.dsl

trait NumericDSL[F[_]] {

  def const[N](n: N): F[N]

  def add[N](a: F[N], b: F[N])(implicit N: Numeric[N]): F[N]

  def subtract[N](a: F[N], b: F[N])(implicit N: Numeric[N]): F[N]

  def multiply[N](a: F[N], b: F[N])(implicit N: Numeric[N]): F[N]

  def divide[N](a: F[N], b: F[N])(implicit N: Fractional[N]): F[N]
}

object NumericDSL {

  implicit val idNumericDSL: NumericDSL[Id] = new NumericDSL[Id] {
    override def const[N](n: N): Id[N] = n

    override def add[N](a: Id[N], b: Id[N])(implicit N: Numeric[N]): Id[N] =
      implicitly[Numeric[N]].plus(a, b)

    override def subtract[N](a: Id[N], b: Id[N])(implicit N: Numeric[N]): Id[N] =
      implicitly[Numeric[N]].minus(a, b)

    override def multiply[N](a: Id[N], b: Id[N])(implicit N: Numeric[N]): Id[N] =
      implicitly[Numeric[N]].times(a, b)

    override def divide[N](a: Id[N], b: Id[N])(implicit N: Fractional[N]): Id[N] =
      implicitly[Fractional[N]].div(a, b)
  }
}
