package com.novocode.continuations

import scala.util.continuations._

object CPSConversions {
  implicit def iteratorToCPSIteratorConversion[T](i: Iterator[T]): CPSIteratorConversion[T] = new CPSIteratorConversion[T](i)
}

trait CPSIterator[T,R] {
  def hasNext: Boolean
  def next(): T
  def foreach(f: T => Unit @cps[R]): Unit @cps[R]
}

class CPSIteratorConversion[T](i: Iterator[T]) {
  def cps[R] = new CPSIterator[T,R] {
    def hasNext: Boolean = i.hasNext
    def next(): T = i.next()
    def foreach(f: T => Unit @cps[R]): Unit @cps[R] = while(hasNext) f(next())
  }
}
