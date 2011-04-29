package org.datamodeling

class Creature(val species: String,
               var health: Int = 50,
               val attack: Int = 5,
               val friendly: Boolean = false) {
  val maxHealth = health
}

object Example6_NamedAndDefault {
  def main(args: Array[String]) {
    val myPetRabbit = new Creature("Rabbit", 12, friendly = true)
    println(myPetRabbit.attack)
  }
}