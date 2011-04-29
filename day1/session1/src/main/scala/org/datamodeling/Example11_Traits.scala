package org.datamodeling

trait Item {
  val name: String
  val weight = 1
}

object Example11_Traits {
  def main(args: Array[String]) {

    object sword extends Item {
      val name = "sword of wonder"
    }
    println(sword.name)
    println(sword.weight)
  }
}