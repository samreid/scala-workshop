package org.frontierdevelopers.scalaworkshop

import java.lang.Integer

case class Player(name: String, gold: Integer, location: Site, items: List[GameObject]) {
  val weapons = Nil

  override def toString = name + ", gold=" + gold + ", location = " + location
}

trait GameObject {
  def getActions: List[Action]

  def getSiteDescription: String

  def getInventoryDescription: String
}

case class Site(name: String, description: String, var objects: List[GameObject]) {
  def this(name: String, description: String) = this (name, description, Nil)
}

case class BlueKey extends GameObject {
  def getActions() = new Action() {
    def update(state: GameState) = {
      val updatedJail: Site = state.map.jail.copy(objects = Nil)
      state.copy(player = state.player.copy(items = BlueKey.this :: state.player.items,
                                            location = updatedJail),
                 map = state.map.copy(jail = updatedJail))
    }

    override def toString = "Take the key when nobody is looking."
  } :: Nil

  def getInventoryDescription = "The blue key from the Scalata Jail"

  def getSiteDescription = "You see a blue key on the stone floor, shimmering in the moonlight."
}

trait Action {
  def update(state: GameState): GameState
}

class ExitAction extends Action {
  def update(state: GameState) = {
    System.exit(0)
    null
  }

  override def toString = "exit game"
}

case class GameMap(jail: Site = new Site("Scalata jail", "Jail in Scalata.  This place reeks of justice", new BlueKey :: Nil)) {
  val townSquare = new Site("Scalata town square", "Town square in Scalata, a nice village with elves or equivalent.  Merchants line the streets and a small protest is forming.")
  val clearing = new Site("Clearing by a pond", "Clearing, near a pond with deer grazing nearby.  No sign of snakes, but there is a foreboding and smelly cave nearby.")
  val cave = new Site("Cave", "Cavernous cave.  A scorpion runs past you, screaming.")
  val links = Map(townSquare -> ( clearing :: jail :: Nil ),
                  clearing -> ( townSquare :: cave :: Nil ),
                  jail -> ( townSquare :: Nil ),
                  cave -> ( Nil )
  )
}

case class GameState(map: GameMap, player: Player) {
  def this(map: GameMap) = this (map, new Player("Dorbax", 0, map.townSquare, Nil))

  def update(input: Action) = input.update(this)

  def options = {
    val travelLinks = for ( link <- map.links(player.location) ) yield {
      TravelTo(link)
    }
    val itemOptions = for ( obj <- player.location.objects; action <- obj.getActions ) yield {
      action
    }
    val systemOptions = new ExitAction :: Nil
    travelLinks ::: itemOptions.toList ::: systemOptions
  }

  override def toString = {
    player.name + ": " + player.gold + " gold, you have: " + player.items.map(_.getInventoryDescription).mkString(",") + "\n" +
    player.location.name +"\n" +
    player.location.description + "\n" +
    player.location.objects.map(_.getSiteDescription).mkString("\n")
  }
}

case class TravelTo(destination: Site) extends Action {
  override def toString = "Travel to " + destination.name

  def update(state: GameState) = state.copy(player = state.player.copy(location = destination))
}

class ConsoleGame {
  def loopGame(state: GameState): Unit = {
    println(state)
    val options = state.options
    val numberedOptions = for ( i <- 0 until options.length ) yield {
      i + ". " + options(i)
    }
    println(numberedOptions.mkString("\n"))
    val line: String = readLine()
    val choice = Integer.parseInt(line)
    loopGame(state.update(options(choice)))
  }
}

object ConsoleGame {
  def main(args: Array[String]) {
    new ConsoleGame().loopGame(new GameState(new GameMap))
  }
}