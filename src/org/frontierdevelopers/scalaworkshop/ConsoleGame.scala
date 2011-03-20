package org.frontierdevelopers.scalaworkshop

import java.lang.Integer

case class Player(gold: Integer = 0, location: Location = Location(0, 0)) {
  val weapons = Nil
}

class Enemy {
}

case class Dragon(location: Location = new Location(0, 0)) {
}

case class Site(location: Location, description: String) {
}

case class Location(x: Integer, y: Integer) {
  def move(dx: Integer, dy: Integer) = copy(x = x.intValue + dx.intValue, y = y.intValue + dy.intValue)
}

case class GameState(player: Player = new Player) {
  val enemies = new Dragon() :: Nil
  val description = Map(Location(0, 0) -> "29th Street Mall.  All is quiet.")

  def update(input: String) = {
    input match {
      case "n" => {
        println("Moving north.")
        copy(player = player.copy(location = player.location.move(0, -1)))
      }
      case "s" => {
        println("Moving south.")
        copy(player = player.copy(location = player.location.move(0, 1)))
      }
      case _ => this
    }
  }

  def options = "options: n,s"

  override def toString = {
    val locationDescription = if ( description.contains(player.location) ) {
      description(player.location)
    }
    else {
      "uncharted location at GPS location: " + player.location
    }
    locationDescription + ", enemies = " + ( for ( e <- enemies if e.location == player.location ) yield {
      e
    } ) + " gold = " + player.gold
  }
}

class ConsoleGame {
  def loopGame(state: GameState): Unit = {
    println(state)
    println(state.options)
    loopGame(state.update(readLine()))
  }
}

object ConsoleGame {
  def main(args: Array[String]) {
    new ConsoleGame().loopGame(new GameState)
  }
}