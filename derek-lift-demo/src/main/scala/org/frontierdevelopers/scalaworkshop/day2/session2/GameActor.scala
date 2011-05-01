/**
 * Copyright 2011, Derek Chen-Becker
 */
package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.{ActorRef, Actor}

sealed trait Action

case class Prompt(status : String, choices : List[Choice])

case class Choice(description : String, agent : ActorRef, action : Action)

case class ClientChoice(player : String, action : Action)

case class Join(name : String, input : ActorRef, display : ActorRef)

case class Move(to : Site) extends Action

case class Display(message : String) extends Action

case class PickUp(thing : Item)

trait Item

class GameActor extends Actor {
  private var currentState = new GameState(GameMap.initialMap)

  def currentChoices(name : String) : List[Choice] = {
    // travelling for now
    val travel : List[Choice] =
      for (link <- currentState.map.links(currentState.players(name).location))
        yield Choice("Move to " + link.description, self, Move(link))

    val currentLocation = currentState.players(name).location

    val chat : List[Choice] =
      for ((otherName,otherPlayer) <- currentState.players.toList
           if otherName != name && otherPlayer.location == currentLocation)
        yield Choice("Chat with " + otherName, otherPlayer.display, Display(name + " says Hi!"))

    travel ::: chat
  }

  def notifyLocationChange(currentPlayer: Player, to: Site, notifyLeave : Boolean = false) {
    val name = currentPlayer.name

    if (notifyLeave) {
      currentState.players.values.filter(p => p.name != name && p.location == currentPlayer.location).foreach {
        _.display ! Display(name + " has left")
      }
    }

    currentState.players.values.filter(p => p.name != name && p.location == to).foreach {
      _.display ! Display(name + " has entered")
    }
  }

  val processAction : PartialFunction[ClientChoice,Prompt] = {
    case ClientChoice(name,Move(to)) => {
      val currentPlayer = currentState.players(name)

      notifyLocationChange(currentPlayer, to, true)

      val player = currentState.players(name).copy(location = to)
      currentState = currentState.copy(players = currentState.players + (name -> player))
      Prompt("You have moved to " +  to.description, currentChoices(name))
    }
  }

  def receive = {
    case Join(name,input,display) => {
      val player = Player(name, 0, GameMap.townSquare, Nil, display)
      currentState = currentState.copy(players = currentState.players + (name -> player))
      notifyLocationChange(player, player.location)
      self.reply_?(Prompt("Welcome!\nYou are in " + player.location.description, currentChoices(name)))
    }
    case choice @ ClientChoice(player, action) if currentState.players.contains(player) =>
      self.reply_?(processAction(choice))
  }
}

