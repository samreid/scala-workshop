package org.datamodeling

object Example10_Lazy {
  val a = 999
  lazy val b = {
    println("hello")
    123
  }

  def main(args: Array[String]) {
    println(a)
    println(b)
  }
}