package org.frontierdevelopers.scalaworkshop

import org.fdgame.solutions.{Armor, Item}

case class NullableCharacter(name : String,
                             var health : Int = 30,
                             var items : List[Item] = Nil,
                             var armor : Armor = null)

object NullArmor {
  def main (args : Array[String]) {
    val fred = NullableCharacter("Fred")

    val attack = 12

    println("Net damage = " + (attack - fred.armor.protection))
  }
}

case class OptionCharacter(name : String,
                           var health : Int = 30,
                           var items : List[Item] = Nil,
                           var armor : Option[Armor] = None)

object OptionArmor {
  def main (args : Array[String]) {
    val ted = OptionCharacter("Ted")

    val attack = 12

    println("Net damage = " + (attack -
      ted.armor.map(_.protection).getOrElse(0)))
  }
}