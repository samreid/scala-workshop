package org.datamodeling

class Dragon {
  val species = "Dragon"
  var health = Int.MaxValue
  val maxHealth = health
  val attack = 42000000
  val friendly = false
}

object Example4_MultipleMonsters {
  def main(args: Array[String]) {
    val myPetDragon = new Dragon
    println(myPetDragon)
    println(myPetDragon.attack)
  }
}