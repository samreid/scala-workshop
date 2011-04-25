package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor
import akka.actor.Actors._
import akka.japi.Creator

object Example3Callbacks {
  def main(args: Array[String]) {
    val actor = actorOf(new Creator[Actor] {
      def create = new Actor {
        protected def receive = {
          case 123 => self reply_? "My favorite number!"
        }
      }
    })
    actor.start()
    actor ! 123
    ( actor !! 123 ) match {
      case Some(x: String) => println("got response " + x)
      case Some(false) => println("got response false")
      case None => println("none")
    }

    val future = actor !!! 123
    val result = future
    println(result)
  }
}