package axon.ml.dsl

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

object Functor {

  implicit def idFunctor: Functor[Id] = new Functor[Id] {
    override def map[A, B](fa: Id[A])(f: A => B): Id[B] = f(fa)
  }

  implicit def thunkFunctor: Functor[Thunk] = new Functor[Thunk] {
    override def map[A, B](fa: Thunk[A])(f: A => B): Thunk[B] = () => f(fa())
  }

  implicit def vectorFunctor: Functor[Vector] = new Functor[Vector] {
    override def map[A, B](fa: Vector[A])(f: A => B): Vector[B] = fa.map(f)
  }
}
