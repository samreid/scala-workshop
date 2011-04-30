package org.datamodeling

class Beast {}

trait Huge {
  val attackMultiplier = 3
}

trait Ugly {
  val fearMultiplier = 5
}

object HugeUglyBeast extends Beast with Huge with Ugly

object Example12_Inheritance {
  def main(args: Array[String]) {
    println("the fear multiplier = " + HugeUglyBeast.fearMultiplier)
  }
}