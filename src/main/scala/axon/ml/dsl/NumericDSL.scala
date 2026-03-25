package axon.ml.dsl

trait NumericDSL[F[_]] {

  def const[N](n: N): F[N]

  def add[N](a: F[N], b: F[N])(implicit N: Numeric[N]): F[N]

  def subtract[N](a: F[N], b: F[N])(implicit N: Numeric[N]): F[N]

  def multiply[N](a: F[N], b: F[N])(implicit N: Numeric[N]): F[N]

  def divide[N](a: F[N], b: F[N])(implicit N: Fractional[N]): F[N]
}

object NumericDSL {

  implicit val idDSL: NumericDSL[Id] = new NumericDSL[Id] {
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

  implicit def funcDSL[X]: NumericDSL[({ type F[A] = X => A })#F] =
    new NumericDSL[({ type F[A] = X => A })#F] {

      def const[N](n: N): X => N =
        _ => n

      def add[N: Numeric](a: X => N, b: X => N): X => N =
        x => implicitly[Numeric[N]].plus(a(x), b(x))

      def subtract[N: Numeric](a: X => N, b: X => N): X => N =
        x => implicitly[Numeric[N]].minus(a(x), b(x))

      def multiply[N: Numeric](a: X => N, b: X => N): X => N =
        x => implicitly[Numeric[N]].times(a(x), b(x))

      def divide[N: Fractional](a: X => N, b: X => N): X => N =
        x => implicitly[Fractional[N]].div(a(x), b(x))
    }
}
