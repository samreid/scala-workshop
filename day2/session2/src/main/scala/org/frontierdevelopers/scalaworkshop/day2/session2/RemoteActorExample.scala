package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor
import akka.actor.Actors._
import akka.japi.Creator
;

/**
 * @author Sam Reid
 */

object RemoteActorExample {
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

class ConsoleActor extends Actor {
  def receive = {
    case x: String => println("Console Actor received string: " + x)
  }
}

object Example1LocalActor {
  def main(args: Array[String]) {
    val actor = actorOf(classOf[ConsoleActor])
    actor.start()
    actor.sendOneWay("hello there")
    actor.sendOneWay("hello again")
    actor ! "hello using !"
  }
}

object Example3 {
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