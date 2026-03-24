package axon.ml.linear

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class VectorMatrixTest extends AnyFlatSpec with Matchers {

  val data: Vector[Vector[Double]] = Vector(
    Vector(1.1, 2.3, 3.3, 4.4, 5.01, 6.02),
    Vector(4, 5, 6, 7, 8, 9),
    Vector(7, 8, 9, 10, 11, 12),
    Vector(10, 11, 12, 13, 14, 15)
  )

  val dataTranspose = data.transpose
  val matrix = VectorMatrix.fromDoubleVector(data)
  val data2 = data.map(x => x.map(y => y * 2))
  val matrix2 = VectorMatrix.fromDoubleVector(data2)

  it should "give basic properties of a vector matrix" in {
    matrix.rows shouldEqual 4
    matrix.columns shouldEqual 6
    matrix.get(1, 1) shouldEqual 1.1
    matrix.get(4, 6) shouldEqual 15.0
  }

  it should "transpose a matrix" in {
    val t = matrix.transpose

    t.rows shouldEqual 6
    t.columns shouldEqual 4
    t.baseData should be(dataTranspose)
  }

  it should "map a matrix with a given function" in {
    val m = matrix.map(x => x * 2)

    m.rows shouldEqual 4
    m.columns shouldEqual 6
    m.baseData should be(data.map(x => x.map(y => y * 2)))
  }

  it should "scale a matrix by a given factor" in {
    val m = matrix.scale(3)

    m.rows shouldEqual 4
    m.columns shouldEqual 6
    m.baseData should be(data.map(x => x.map(y => y * 3)))
  }

  it should "add two matrices" in {
    val m = matrix.add(matrix2)
    m.rows shouldEqual 4
    m.columns shouldEqual 6

    m.baseData should be {
      data.zip(data2).map { case (a, b) => a.zip(b).map { case (x, y) => x + y } }
    }
  }

  it should "subtract two matrices" in {
    val m = matrix2.subtract(matrix)
    m.rows shouldEqual 4
    m.columns shouldEqual 6

    m.baseData should be {
      data2.zip(data).map { case (a, b) => a.zip(b).map { case (x, y) => x - y } }
    }
  }

  it should "dot product two matrices" in {
    val d2: Vector[Vector[Double]] =
      Vector(Vector(1.0), Vector(0), Vector(1), Vector(0), Vector(1), Vector(0))
    val m2 = VectorMatrix.fromDoubleVector(d2)
    val result = matrix.dot(m2)

    result.rows shouldEqual 4
    result.columns shouldEqual 1
    result.baseData should be(Vector(Vector(9.41), Vector(18.0), Vector(27.0), Vector(36.0)))
  }

  it should "sum the rows of a matrix" in {
    val sum = matrix.rowSums(2)

    sum shouldEqual 39.0
  }

  it should "sum the columns of a matrix" in {
    val sum = matrix.columnSums(2)

    sum shouldEqual 26.3
  }

  it should "calculate the squared error" in {
    val error = matrix.squaredError(5.0)

    BigDecimal(error).setScale(4, BigDecimal.RoundingMode.HALF_UP) shouldEqual BigDecimal(551.7905)
  }
}
