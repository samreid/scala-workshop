package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.ActorRef

/**
 * This represents our (very simplified) players in the
 * game. We keep track of the player's name, as well as their location
 * and the actors for this player's client that we want to use for
 * communication.
 */
case class Player(name: String, location: Site, display : ActorRef, input : ActorRef) {
  override def toString = name + ", location = " + location.name
}

/**
 * This represents a location in the game
 */
case class Site(name: String, description: String)

/**
 * This represents our map of the game via a mapping
 * of a given location to the other locations you may
 * reach from there.
 */
case class GameMap(links: Map[Site, List[Site]])

/**
 * This is the companion object to our GameMap case class. Here
 * we statically define our locations our default map for
 * the game.
 */
object GameMap {
  val jail = Site("Scalata jail", "Jail in Scalata.  This place reeks of justice")
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

/**
 * This represents the current state of our game, with
 * the map and a mapping of player names to players (essentially
 * our registry of clients).
 */
case class GameState(map: GameMap, players: Map[String,Player] = Map())
