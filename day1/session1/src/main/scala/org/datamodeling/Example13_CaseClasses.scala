package org.datamodeling

trait Item {val name: String}

case class Weapon(name: String, attack: Int)
        extends Item

object Example13_CaseClasses {
  def main(args: Array[String]) {
    val sword = new Weapon("sword of wonder", 999)
    println(sword)
  }
}