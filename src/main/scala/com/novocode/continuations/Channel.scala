package com.novocode.continuations

import scala.util.continuations._

class Channel[T] {
  private[this] var cont: (T => Unit @cps[Unit]) = Channel.defaultCont
  def send(v: T) = reset { cont(v) }
  def receive(): T @cps[Unit] = shift { k: (T => Unit) => cont = { v:T => cont = Channel.defaultCont; k(v) } }
}

object Channel {
  private val defaultCont: (Any => Unit @cps[Unit]) =
    { _:Any => throw new RuntimeException("No receiver registered on channel") }
  def apply[T](f: (Channel[T] => Unit @cps[Unit])): Channel[T] = { 
    val ch = new Channel[T]
    reset { f(ch) }
    ch
  }
}
