package org.datamodeling

/**
 * @author Sam Reid
 */

object Example1_Values {
  def main(args: Array[String]) {
    val species = "Grue"
    var health = Int.MaxValue

    // initial health equals max health
    val maxHealth = health
    val attack = 42000000

    // Definitely not friendly
    val friendly = false
    println(species)
    println(attack)
  }
}