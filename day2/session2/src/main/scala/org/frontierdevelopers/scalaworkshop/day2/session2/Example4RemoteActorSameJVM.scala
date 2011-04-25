package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor
import akka.actor.Actors._

object Example4RemoteActorSameJVM {
  def main(args: Array[String]) {
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
    val result = actor !! "Hello afterwards"
    println(result)
  }
}