package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor
import akka.actor.Actors._
import akka.japi.Creator

object LocalActorExamples

object Example2ManyTypes {
  def main(args: Array[String]) {
    val actor = actorOf(new Creator[Actor] {
      def create = new Actor {
        protected def receive = {
          case 123 => self reply_? "got your 123"
          case x: String => println("received string: " + x)
        }
      }
    })
    actor.start()
    actor.sendOneWay("hello there, second time")
    actor.sendOneWay(123.asInstanceOf[AnyRef])
    actor ! "hello !"
    ( actor !! 123.asInstanceOf[AnyRef] ) match {
      case Some(x: String) => println(" got response " + x)
      case Some(false) => println(" got response false")
      case None => println("none")
    }

    val future = actor !!! 123.asInstanceOf[AnyRef]
    val result = future
    println(result)
  }
}