package org.datamodeling

object Grue {
  val species = "Grue"
  var health = Int.MaxValue
  val maxHealth = health
  val attack = 42000000
  val friendly = false
}

object Example3_OurFirstMonster {
  def main(args: Array[String]) {
    println(Grue.attack)
  }
}