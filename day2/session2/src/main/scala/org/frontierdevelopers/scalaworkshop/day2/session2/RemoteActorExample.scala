package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor
import akka.actor.Actors._
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