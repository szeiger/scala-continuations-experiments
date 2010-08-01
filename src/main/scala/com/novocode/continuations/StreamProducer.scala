package com.novocode.continuations

import scala.util.continuations._

trait StreamProducer[T] {
  def stream: Stream[T] = reset { run; Stream.Empty }
  def run: Unit @cps[Stream[T]]
  def emit(v: T): Unit @cps[Stream[T]] = shift { k: (Unit => Stream[T]) => Stream.cons(v, k()) }
}

object StreamProducer {
  def apply[T](f: StreamProducer[T] => Unit @cps[Stream[T]]): Stream[T] =
    new StreamProducer[T] { def run = f(this) }.stream
}
