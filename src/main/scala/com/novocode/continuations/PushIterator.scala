package com.novocode.continuations

import scala.util.continuations._

abstract class PushIterator[T] extends Iterator[T] {
  protected var cont: Unit => Unit @cps[Unit]
  private[this] var value: T = _
  private[this] var hasValue = false
  private[this] def tryNext() = if((cont ne null) && !hasValue) reset { cont(); }
  def hasNext = {
    tryNext()
    cont ne null
  }
  def next() = {
    tryNext()
    if(cont eq null) throw new NoSuchElementException("next on empty iterator")
    hasValue = false
    value
  }
  def emit(v: T): Unit @cps[Unit] = shift { k: (Unit => Unit) =>
    value = v
    hasValue = true
    cont = { _ => k() }
  }
  def finish() = cont = null
}

object PushIterator {
  def apply[T](f: PushIterator[T] => Unit @cps[Unit]): Iterator[T] = new PushIterator[T] {
    protected var cont = { _:Unit => f(this); finish() }
  }
}
