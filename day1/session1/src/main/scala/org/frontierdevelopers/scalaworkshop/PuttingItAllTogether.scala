package org.frontierdevelopers.scalaworkshop

/**
 * @author Sam Reid
 */

class PuttingItAllTogether

//Putting it all together
case class Key(color: String)

case class Potion(healing: Int)

case class Game {
  val player = new Player2
  val worldMap = Map("jail" -> List("town square"),
                     "town square" -> List("jail", "cave", "Scalata"))

  override def toString = "player = " + player + ", map = " + worldMap
}

case class Player2 {
  val name = "hero"
  var items = Potion(-5) :: Potion(123) :: Key("blue") :: Nil

  override def toString = "name = " + name + ", items: " + items.mkString(", ")
}

object TestMain {
  def main(args: Array[String]) {
    println(new Game)
  }
}

object Grue{
  val species = "Grue"
  val strength = 456
  val maxHealth = 200
  val health = maxHealth
  val attackDamage = 42000000
  val friendly = false
}

object MyAwesomeGame{
  def main(args: Array[String]) {
    println(Grue)
  }
}