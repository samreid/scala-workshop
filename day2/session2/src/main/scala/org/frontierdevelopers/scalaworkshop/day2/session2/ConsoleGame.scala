package org.frontierdevelopers.scalaworkshop.day2.session2

import java.lang.Integer
import akka.actor.ActorRef

case class Player(name: String, gold: Integer, location: Site, items: List[GameObject], display : ActorRef) {
  val weapons = Nil

  override def toString = name + ", gold=" + gold + ", location = " + location
}

trait GameObject {
  def actions: List[GameAction]

  def getSiteDescription: String

  def getInventoryDescription: String
}

case class Site(name: String, description: String, var objects: List[GameObject] = Nil) {
  def this(name: String, description: String) = this (name, description, Nil)
}

trait GameAction {
  def update(state: GameState): (GameState, String)
}

class ExitAction extends GameAction {
  def update(state: GameState) = {
    System.exit(0)
    null
  }

  override def toString = "exit game"
}

case class GameMap(links: Map[Site, List[Site]]) {
  def update(old: Site, newsite: Site ): GameMap = GameMap(
    links.foldLeft(Map.empty[Site, List[Site]]) {
      case (m, (from, to)) => m + ((if (from == old) newsite else old) -> to.map(t => if (t == old) newsite else old))
    }
  )
}

object GameMap {
  val jail = Site("Scalata jail", "Jail in Scalata.  This place reeks of justice", Nil)
  val townSquare = Site("Scalata town square", "Town square in Scalata, a nice village with elves or equivalent.  Merchants line the streets and a small protest is forming.")
  val clearing = Site("Clearing by a pond", "Clearing, near a pond with deer grazing nearby.  No sign of snakes, but there is a foreboding and smelly cave nearby.")
  val cave = Site("Cave", "Cavernous cave.  A scorpion runs past you, screaming.")

  val initialMap = GameMap(
    Map(
      townSquare -> ( clearing :: jail :: Nil ),
      clearing -> ( townSquare :: cave :: Nil ),
      jail -> ( townSquare :: Nil ),
      cave -> ( clearing :: Nil )
    )
  )
}


//case class GameMap(jail: Site = new Site("Scalata jail", "Jail in Scalata.  This place reeks of justice", new BlueKey :: Nil)) {
//  val townSquare = new Site("Scalata town square", "Town square in Scalata, a nice village with elves or equivalent.  Merchants line the streets and a small protest is forming.")
//  val clearing = new Site("Clearing by a pond", "Clearing, near a pond with deer grazing nearby.  No sign of snakes, but there is a foreboding and smelly cave nearby.")
//  val cave = new Site("Cave", "Cavernous cave.  A scorpion runs past you, screaming.")
//  val links = Map(townSquare -> ( clearing :: jail :: Nil ),
//                  clearing -> ( townSquare :: cave :: Nil ),
//                  jail -> ( townSquare :: Nil ),
//                  cave -> ( Nil )
//  )
//}

case class GameState(map: GameMap, players: Map[String,Player] = Map())

