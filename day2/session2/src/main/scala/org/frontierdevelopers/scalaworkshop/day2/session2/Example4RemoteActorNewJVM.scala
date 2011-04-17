package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor
import akka.actor.Actors._
import java.lang.String

object Example4RemoteActorNewJVM {
  val service: String = "example-4-multipleJVM-server"
}

object Server {
  def main(args: Array[String]) {
    // server code
    class HelloWorldActor extends Actor {
      def receive = {
        case msg => {
          println("Server received message: " + msg)
          self reply ( msg + " World" )
        }
      }
    }
    remote.start("localhost", 9999).register(Example4RemoteActorNewJVM.service, actorOf(classOf[HelloWorldActor]))
  }
}

object Client {
  def main(args: Array[String]) {
    val actor = remote.actorFor(Example4RemoteActorNewJVM.service, "localhost", 9999)
    val result = actor !! "Client says hello"
    println("received message from server: "+result)
  }
}