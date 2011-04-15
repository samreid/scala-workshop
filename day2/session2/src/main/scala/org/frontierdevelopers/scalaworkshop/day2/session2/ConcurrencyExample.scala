package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor
import akka.actor.Actors._
import akka.japi.Creator
;

/**
 * @author Sam Reid
 */

object ConcurrencyExample {
  def main(args: Array[String]) {
    println("hello")

    // server code
    class HelloWorldActor extends Actor {
      def receive = {
        case msg => {
          println("hello there, message received")
          self reply ( msg + " World" )
        }
      }
    }
    remote.start("localhost", 9999).register("hello-service", actorOf(classOf[HelloWorldActor]))

    // client code
    val actor = remote.actorFor("hello-service", "localhost", 9999)
    val result = actor !! "Hello afterwanrds"
    println(result)

  }
}

object Example2 {
  def main(args: Array[String]) {
    println("hello2")
    val x = Map("sword" -> 123)
    val y = x.toList.sortWith(_._2 < _._2)
    println("y = " + y)
  }
}

class MyActor extends Actor {
  def receive = {
    case x: String => {
      println("received string: " + x)
    }
  }
}

object Example3 {
  def main(args: Array[String]) {
    val actor = actorOf(new Creator[Actor] {
      def create = new Actor {
        protected def receive = {
          case x: String => println("received string: " + x)
        }
      }
    })
    actor.start()
    actor.sendOneWay("hello there, second time")
  }
}