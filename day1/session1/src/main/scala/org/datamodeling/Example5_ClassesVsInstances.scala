package org.datamodeling

class MyCreature2(val species: String,
                  var health: Int,
                  val attack: Int,
                  val friendly: Boolean) {
  val maxHealth = health
}

object Example5_ClassesVsInstances {
  def main(args: Array[String]) {
    val myPetGrue = new MyCreature2("Grue", Int.MaxValue, 42000000, false)
    println(myPetGrue)
    println(myPetGrue.species)
  }
}

