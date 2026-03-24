package axon.ml.dsl

sealed trait Expr[N]

case class Const[N](n: N) extends Expr[N]

case class Add[N](a: Expr[N], b: Expr[N]) extends Expr[N]

case class Sub[N](a: Expr[N], b: Expr[N]) extends Expr[N]

case class Mul[N](a: Expr[N], b: Expr[N]) extends Expr[N]

case class Div[N](a: Expr[N], b: Expr[N]) extends Expr[N]
