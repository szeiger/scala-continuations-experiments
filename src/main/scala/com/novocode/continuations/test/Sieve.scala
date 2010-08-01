package com.novocode.continuations.test

import scala.util.continuations._
import com.novocode.continuations._
import com.novocode.continuations.CPSConversions._

/**
 * Sieve of Eratosthenes in CSP (Communicating Sequential
 * Processes) style using Doug McIlroy's algorithm.
 * 
 * @see http://swtch.com/~rsc/thread/
 */
object Sieve {
  def main(args : Array[String]) : Unit = {

    def create(): Channel[Int] = Channel[Int] { self =>
      val p = self.receive()
      print(p+" ")
      val right = create()
      while(true) {
        val n = self.receive()
        if(n % p != 0) right.send(n)
      }
    }

    val first = create()
    for(i <- 2 to 100) first.send(i)
    println()
  }
}
