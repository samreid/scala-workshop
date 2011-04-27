package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor

//Simple actor implementation with built-in-types
class ConsoleActor extends Actor {
  def receive = {
    case x: String => println("Console Actor received string: " + x)
    case 123 => println("Wow, it's my favorite number")
  }
}

import akka.actor.Actors._
object SendConsoleActor {
  def main(args: Array[String]) {
    val actor = actorOf(classOf[ConsoleActor])
    actor.start()
    actor.sendOneWay("Why hello there!")
    actor ! 123
    actor ! "Bye!"
  }
}