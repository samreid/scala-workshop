package org.datamodeling

abstract class MyCreature(val species: String,
                        var health: Int)

object Example8_Abstract {
  def main(args: Array[String]) {
    //won't compile:
    // val x = new Creature("genie",999)
  }
}