package axon.ml.linear

import axon.ml.dsl.{Functor, Id, NumericDSL, Thunk, ThunkDSL}

class VectorMatrix[F[_], N](
    val data: Vector[Vector[F[N]]]
)(implicit val F: Functor[F], val N: Numeric[N])
    extends Matrix[Vector, F, N] {
  def baseData: Vector[Vector[F[N]]] = data

  override def rows: Long =
    data.length.toLong

  override def columns: Long =
    if (data.isEmpty) 0L else data.head.length.toLong

  override def get(row: Long, column: Long): F[N] =
    data(row.toInt - 1)(column.toInt - 1)

  override def map[T](f: N => T)(implicit F: Functor[F], NT: Numeric[T]): Matrix[Vector, F, T] =
    new VectorMatrix[F, T](
      data.map(row => row.map(fn => F.map(fn)(f)))
    )

  override def transpose: Matrix[Vector, F, N] =
    new VectorMatrix(
      data.transpose
    )

  override def scale(factor: N)(implicit dsl: NumericDSL[F]): Matrix[Vector, F, N] = {
    val fFactor = dsl.const(factor)

    new VectorMatrix(
      data.map { row =>
        row.map { value =>
          dsl.multiply(value, fFactor)
        }
      }
    )
  }

  override def add(
      other: Matrix[Vector, F, N]
  )(implicit dsl: NumericDSL[F]): Matrix[Vector, F, N] = {

    val o = other.asInstanceOf[VectorMatrix[F, N]]

    new VectorMatrix(
      data.zip(o.data).map { case (rowA, rowB) =>
        rowA.zip(rowB).map { case (a, b) =>
          dsl.add(a, b)
        }
      }
    )
  }

  override def subtract(
      other: Matrix[Vector, F, N]
  )(implicit dsl: NumericDSL[F]): Matrix[Vector, F, N] = {

    val o = other.asInstanceOf[VectorMatrix[F, N]]

    new VectorMatrix(
      data.zip(o.data).map { case (rowA, rowB) =>
        rowA.zip(rowB).map { case (a, b) =>
          dsl.subtract(a, b)
        }
      }
    )
  }

  override def dot(
      other: Matrix[Vector, F, N]
  )(implicit dsl: NumericDSL[F]): Matrix[Vector, F, N] = {

    val o = other.asInstanceOf[VectorMatrix[F, N]]
    val otherT = o.data.transpose

    val zero = dsl.const(N.zero)

    new VectorMatrix(
      data.map { row =>
        otherT.map { col =>
          row.zip(col).foldLeft(zero) { case (acc, (a, b)) =>
            val prod = dsl.multiply(a, b)
            dsl.add(acc, prod)
          }
        }
      }
    )
  }

  override def rowSums(row: Long)(implicit dsl: NumericDSL[F], N: Numeric[N]): F[N] = {

    val zero = dsl.const(N.zero)

    data(row.toInt - 1).foldLeft(zero) { (acc, v) =>
      dsl.add(acc, v)
    }
  }

  override def columnSums(column: Long)(implicit dsl: NumericDSL[F], N: Numeric[N]): F[N] = {

    val zero = dsl.const(N.zero)

    data.map(_(column.toInt - 1)).foldLeft(zero) { (acc, v) =>
      dsl.add(acc, v)
    }
  }

  override def squaredError(comparator: N)(implicit dsl: NumericDSL[F], N: Numeric[N]): F[N] = {

    val comp = dsl.const(comparator)
    val zero = dsl.const(N.zero)

    data.flatten.foldLeft(zero) { (acc, v) =>
      val diff = dsl.subtract(v, comp)
      val sq = dsl.multiply(diff, diff)
      dsl.add(acc, sq)
    }
  }

}

object VectorMatrix {
  import Functor._

  implicitly[Numeric[Double]]
  implicitly[Numeric[BigDecimal]]

  def fromDoubleVector(data: Vector[Vector[Double]]): Matrix[Vector, Id, Double] =
    new VectorMatrix[Id, Double](data)

  def fromBigDecimalVector(data: Vector[Vector[BigDecimal]]): Matrix[Vector, Id, BigDecimal] =
    new VectorMatrix[Id, BigDecimal](data)

  def liftThunk[N](data: Vector[Vector[N]])(implicit N: Numeric[N]): Matrix[Vector, Thunk, N] =
    new VectorMatrix[Thunk, N](ThunkDSL.liftThunk[N](data))
}
