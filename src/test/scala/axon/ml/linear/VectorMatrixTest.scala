package axon.ml.linear

import axon.ml.dsl.ThunkDSL.thunkDSL
import axon.ml.dsl.{Thunk, ThunkDSL}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class VectorMatrixTest extends AnyFunSpec with Matchers {

  describe("Matrix using Vectors of Doubles") {
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

    it("give basic properties of a vector matrix") {
      matrix.rows shouldEqual 4
      matrix.columns shouldEqual 6
      matrix.get(1, 1) shouldEqual 1.1
      matrix.get(4, 6) shouldEqual 15.0
    }

    it("transpose a matrix") {
      val t = matrix.transpose

      t.rows shouldEqual 6
      t.columns shouldEqual 4
      t.baseData should be(dataTranspose)
    }

    it("map a matrix with a given function") {
      val m = matrix.map(x => x * 2)

      m.rows shouldEqual 4
      m.columns shouldEqual 6
      m.baseData should be(data.map(x => x.map(y => y * 2)))
    }

    it("scale a matrix by a given factor") {
      val m = matrix.scale(3)

      m.rows shouldEqual 4
      m.columns shouldEqual 6
      m.baseData should be(data.map(x => x.map(y => y * 3)))
    }

    it("add two matrices") {
      val m = matrix.add(matrix2)
      m.rows shouldEqual 4
      m.columns shouldEqual 6

      m.baseData should be {
        data.zip(data2).map { case (a, b) => a.zip(b).map { case (x, y) => x + y } }
      }
    }

    it("subtract two matrices") {
      val m = matrix2.subtract(matrix)
      m.rows shouldEqual 4
      m.columns shouldEqual 6

      m.baseData should be {
        data2.zip(data).map { case (a, b) => a.zip(b).map { case (x, y) => x - y } }
      }
    }

    it("dot product two matrices") {
      val d2: Vector[Vector[Double]] =
        Vector(Vector(1.0), Vector(0), Vector(1), Vector(0), Vector(1), Vector(0))
      val m2 = VectorMatrix.fromDoubleVector(d2)
      val result = matrix.dot(m2)

      result.rows shouldEqual 4
      result.columns shouldEqual 1
      result.baseData should be(Vector(Vector(9.41), Vector(18.0), Vector(27.0), Vector(36.0)))
    }

    it("sum the rows of a matrix") {
      val sum = matrix.rowSums(2)

      sum shouldEqual 39.0
    }

    it("sum the columns of a matrix") {
      val sum = matrix.columnSums(2)

      sum shouldEqual 26.3
    }

    it("calculate the squared error") {
      val error = matrix.squaredError(5.0)

      BigDecimal(error).setScale(4, BigDecimal.RoundingMode.HALF_UP) shouldEqual BigDecimal(
        551.7905
      )
    }
  }

  describe("Matrix using Vectors of BigDecimals") {
    val data = Vector(
      Vector(BigDecimal(1.1), BigDecimal(2.3), BigDecimal(3.3)),
      Vector(BigDecimal(4), BigDecimal(5), BigDecimal(6)),
      Vector(BigDecimal(4.4), BigDecimal(5.01), BigDecimal(6.02))
    )

    val dataTranspose = data.transpose
    val matrix = VectorMatrix.fromBigDecimalVector(data)
    val data2 = data.map(x => x.map(y => y * 2))
    val matrix2 = VectorMatrix.fromBigDecimalVector(data2)

    it("give basic properties of a vector matrix") {
      matrix.rows shouldEqual 3
      matrix.columns shouldEqual 3
      matrix.get(1, 1) shouldEqual BigDecimal(1.1)
      matrix.get(2, 2) shouldEqual BigDecimal(5)
    }

    it("transpose a matrix") {
      val t = matrix.transpose

      t.rows shouldEqual 3
      t.columns shouldEqual 3
      t.baseData should be(dataTranspose)
    }

    it("map a matrix with a given function") {
      val m = matrix.map(x => x * 2)

      m.rows shouldEqual 3
      m.columns shouldEqual 3
      m.baseData should be(data2)
    }

    it("scale a matrix by a given factor") {
      val m = matrix.scale(3)

      m.rows shouldEqual 3
      m.columns shouldEqual 3
      m.baseData should be(data.map(x => x.map(y => y * 3)))
    }

    it("add two matrices") {
      val m = matrix.add(matrix2)
      m.rows shouldEqual 3
      m.columns shouldEqual 3

      m.baseData should be {
        data.zip(data2).map { case (a, b) => a.zip(b).map { case (x, y) => x + y } }
      }
    }

    it("subtract two matrices") {
      val m = matrix2.subtract(matrix)
      m.rows shouldEqual 3
      m.columns shouldEqual 3

      m.baseData should be {
        data2.zip(data).map { case (a, b) => a.zip(b).map { case (x, y) => x - y } }
      }
    }

    it("dot product two matrices") {
      val d2: Vector[Vector[BigDecimal]] =
        Vector(Vector(BigDecimal(1)), Vector(BigDecimal(0)), Vector(BigDecimal(1)))
      val m2 = VectorMatrix.fromBigDecimalVector(d2)
      val result = matrix.dot(m2)

      result.rows shouldEqual 3
      result.columns shouldEqual 1
      result.baseData should be(Vector(Vector(4.4), Vector(10), Vector(10.42)))
    }

    it("sum the rows of a matrix") {
      val sum = matrix.rowSums(2)

      sum shouldEqual BigDecimal(15)
    }

    it("sum the columns of a matrix") {
      val sum = matrix.columnSums(2)

      sum shouldEqual BigDecimal(12.31)
    }

    it("calculate the squared error") {
      val error = matrix.squaredError(5.0)

      error.setScale(4, BigDecimal.RoundingMode.HALF_UP) shouldEqual BigDecimal(
        28.7905
      )
    }
  }

  describe("Matrix using Thunks") {
    val m1 = VectorMatrix.liftThunk[Double](Vector(Vector(1.1, 2.3, 3.3), Vector(4, 5, 6)))
    val m2 = VectorMatrix.liftThunk[Double](Vector(Vector(4.4, 5.01), Vector(7, 8), Vector(9, 10)))

    it("should give basic properties of a vector matrix using Thunks") {
      m1.rows shouldEqual 2
      m1.columns shouldEqual 3
      m1.get(1, 1)() shouldEqual 1.1
      m1.get(2, 2)() shouldEqual 5
    }

    it("transpose a matrix giving a thunk") {
      val t = m1.transpose

      t.rows shouldEqual 3
      t.columns shouldEqual 2
      ThunkDSL.execute(t.baseData) should be(Vector(Vector(1.1, 4), Vector(2.3, 5), Vector(3.3, 6)))
    }

    it("should do a dot product lazily") {
      val result = m1.dot(m2)

      ThunkDSL
        .execute(result.baseData)
        .map(v => v.map(d => BigDecimal(d).setScale(4, BigDecimal.RoundingMode.HALF_UP))) should be(
        Vector(Vector(50.64, 56.911), Vector(106.6, 120.04))
      )
    }

    it("should show laziness") {
      var counter = 0
      def tracked(n: Double): Thunk[Double] = () => {
        counter += 1
        n
      }

      val m = new VectorMatrix[Thunk, Double](Vector(Vector(tracked(1.1))))
      val scaled = m.scale(2.0)

      assert(counter == 0)
      scaled.get(1, 1)() shouldEqual 2.2
      assert(counter == 1)
    }
  }

  describe("Matrix using Functors") {

    val f1 = (x: Double) => x + 1
    val f2 = (x: Double) => x * 2
    val f3 = (x: Double) => x - 3
    val f4 = (x: Double) => x * x

    val functions: Vector[Vector[Double => Double]] = Vector(
      Vector(f1, f2),
      Vector(f3, f4)
    )
    val matrix = VectorMatrix.fromFunctionVectors(functions)

    it("should scale a matrix using a function") {
      val scaled = matrix.scale(2.0)
      val input = 5.0

      val result =
        (0 until (scaled.rows.toInt)).map { r =>
          (0 until (scaled.columns.toInt)).map { c =>
            scaled.get(r + 1, c + 1)(input)
          }.toVector
        }.toVector

      assert(result == Vector(Vector(12.0, 20.0), Vector(4.0, 50.0)))
    }
  }

}
