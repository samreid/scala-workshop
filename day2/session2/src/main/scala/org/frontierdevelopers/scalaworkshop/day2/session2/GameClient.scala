/**
 * Copyright 2011, Derek Chen-Becker
 */
package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.{ActorRef, Actors}
import annotation.tailrec


object GameClient {
  def main (args : Array[String]) {
    val game = Actors.actorOf(classOf[GameActor]).start
    val input = Actors.actorOf(classOf[ClientInput]).start
    val display = Actors.actorOf(classOf[ClientDisplay]).start

    println("What is your name?")

    joinGame(game, input, display, readLine())
  }

  @tailrec
  def joinGame (game : ActorRef, input : ActorRef, display : ActorRef, name : String) {
    game !! Join(name, input, display) match {
      case Some(p : Prompt) => println("Joined!"); input ! name; input ! p
      case None => {
        println("Time out joining game!")
        joinGame(game, input, display, name)
      }
    }
  }
}