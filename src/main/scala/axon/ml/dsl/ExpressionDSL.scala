package axon.ml.dsl

trait ExpressionDSL extends NumericDSL[Expr] {

  def const[N](n: N) = Const(n)

  def add[N](a: Expr[N], b: Expr[N]): Expr[N] = Add(a, b)

  def subtract[N](a: Expr[N], b: Expr[N]) = Sub(a, b)

  def multiply[N](a: Expr[N], b: Expr[N]) = Mul(a, b)

  def divide[N](a: Expr[N], b: Expr[N]) = Div(a, b)
}
