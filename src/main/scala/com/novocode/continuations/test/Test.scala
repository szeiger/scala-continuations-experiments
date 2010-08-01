package com.novocode.continuations.test

import scala.util.continuations._
import com.novocode.continuations._
import com.novocode.continuations.CPSConversions._

object Test {
  def main(args : Array[String]) : Unit = {

    var v = StreamProducer[Int] { p =>
      var i=1
      while(i <= 3) {
        p.emit(i)
        i += 1
      }
      (4 to 5).iterator.cps foreach p.emit
    }
    for(i <- v) println("stream "+i)

    var w = PushIterator[Int] { p =>
      p.emit(1)
      p.emit(2)
      p.emit(3)
    }
    for(i <- w) println("iterator "+i)

    val ch = Channel[Int] { ch =>
      while(true) {
        println("Channel received: "+ch.receive())
      }
    }

    ch.send(1)
    ch.send(2)
  }
}
