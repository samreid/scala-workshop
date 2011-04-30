package org.datamodeling

class Monster(species: String,
              val name: String,
              health: Int = 50,
              attack: Int = 10,
              val intelligence: Int = 10)
        extends Creature(species, health, attack, false)

object Example7_Specializing {
  def main(args: Array[String]) {
    val monster = new Monster("centaur", "larry", attack = 999, intelligence = 2)
    println(monster)
  }
}