package org.datamodeling

object Example2_Evolve {
  def main(args: Array[String]) {
    val species = "Grue"
    var health = Int.MaxValue
    val maxHealth = health
    // to start
    val attack = 42000000
    val friendly = false
    // Definitely not
    println("initial health = " + Grue.health)
    Grue.health = 123
    println("changed health = " + Grue.health)
    //Won't compile
    //Grue.friendly = true
  }
}