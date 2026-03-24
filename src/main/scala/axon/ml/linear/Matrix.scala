package axon.ml.linear

import axon.ml.dsl.{Functor, NumericDSL}

/** A trait for representing a matrix.
  *
  * @tparam F the computation/collection context in the matrix
  *          eg. Id, Expr, Thunk
  * @tparam N the type of the elements in the matrix
  *          eg. Double, Long
  */
trait Matrix[C[_], F[_], N] {

  def baseData: C[C[F[N]]]

  def rows: Long

  def columns: Long

  def get(row: Long, column: Long): F[N]

  def map[T](f: N => T)(implicit F: Functor[F], NT: Numeric[T]): Matrix[C, F, T]

  def transpose: Matrix[C, F, N]

  def scale(factor: N)(implicit dsl: NumericDSL[F]): Matrix[C, F, N]

  def dot(other: Matrix[C, F, N])(implicit dsl: NumericDSL[F]): Matrix[C, F, N]

  def add(other: Matrix[C, F, N])(implicit dsl: NumericDSL[F]): Matrix[C, F, N]

  def subtract(other: Matrix[C, F, N])(implicit dsl: NumericDSL[F]): Matrix[C, F, N]

  def rowSums(row: Long)(implicit dsl: NumericDSL[F], N: Numeric[N]): F[N]

  def columnSums(column: Long)(implicit dsl: NumericDSL[F], N: Numeric[N]): F[N]

  def squaredError(comparator: N)(implicit dsl: NumericDSL[F], N: Numeric[N]): F[N]

}
