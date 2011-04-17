package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor
import akka.actor.Actors._

class ConsoleActor extends Actor {
  def receive = {
    case x: String => println("Console Actor received string: " + x)
    case 123 => println("Wow, it's my favorite number")
  }
}

object Example1LocalActor {
  def main(args: Array[String]) {
    val actor = actorOf(classOf[ConsoleActor])
    actor.start()
    actor ! "Why hello there!"
    actor ! 123
  }
}