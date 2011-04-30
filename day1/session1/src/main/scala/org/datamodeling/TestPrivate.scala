package org.datamodeling

object TestPrivate {
  def main(args: Array[String]) {
    class Creature(val species: String,
                   private var health: Int)

    val c = new Creature("dragon",123)
    c.species
  }
}