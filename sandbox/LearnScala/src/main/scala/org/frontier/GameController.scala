/**
 * Copyright 2011, Derek Chen-Becker
 */

package org.frontier

import akka.actor.{ActorRef, Actor}
import Actor._

object Controllers {
  val game = actorOf[GameController]
  val inventory = actorOf[InventoryController]

  remote.register("game", game)
  remote.register("inventory", inventory)

  val gameAgent = "game"
  val inventoryAgent = "inventory"
}

sealed trait Request
case class Action(description : String, agent : String, message : Request)
case class Prompt(message : String, choices : List[Action])
case class Info(message : String)

case class Join(name : String) extends Request

class GameController extends Actor {
  private[this] var players = Map[String,Character]()

  def defaultActions (name : String) : List[Action] = {
    List(Action("Check inventory", Controllers.inventoryAgent, GetInventory(players(name))))
  }

  def receive = {
    case Join(player) => if (players.contains(player)) {
      self.reply_?("That name is taken")
    } else {
      players += (player -> new Character("Human", player))
      self.reply_?(Prompt("Not much to do now", defaultActions(player)))
    }
  }
}



case class GetInventory(subject : HasInventory) extends Request
case class AddToInventory(subject : HasInventory, item : Item) extends Request
case class RemoveFromInventory(subject : HasInventory, item : Item) extends Request
case class TransferItem(item : Item, from : HasInventory, to : HasInventory) extends Request

class InventoryController extends Actor {
  private[this] var inventory = Map[HasInventory, List[Item]]().withDefaultValue(Nil)
  def receive = {
    case GetInventory(subject) => {
      self.reply_?(inventory(subject) match {
        case Nil => "You aren't carrying anything"
        case items => items.mkString("You're carrying: " , ", ", "")
      })
    }
    case AddToInventory(subject, item) => {
      val newInventory = item :: inventory(subject)
      inventory += (subject -> newInventory)
      self.reply_?(newInventory)
    }
    case TransferItem(item, from, to) => {
      val (matchingItems, otherItems) = inventory(from).partition(_ == item)
      val moved = matchingItems.headOption.map {
        found => inventory ++= List(from -> (matchingItems.drop(1) ::: otherItems),
                                    to -> (found :: inventory(to))); true
      } getOrElse false

      self.reply_?(moved)
    }
  }
}


