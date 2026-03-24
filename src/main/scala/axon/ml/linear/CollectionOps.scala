package axon.ml.linear

trait CollectionOps[C[_]] {

  def map[A, B](ca: C[A])(f: A => B): C[B]

  def zip[A, B](ca: C[A], cb: C[B]): C[(A, B)]

  def index[A](ca: C[A], i: Int): A
}
