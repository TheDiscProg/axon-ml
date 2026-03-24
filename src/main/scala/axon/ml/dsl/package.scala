package axon.ml

package object dsl {

  type Id[A] = A

  type Thunk[A] = () => A
}
