package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actors._
import akka.japi.Creator
import akka.actor.Actor
import collection.mutable.HashSet

case class Attack(target: String, damage: Int)

case class Login(target: String)

object Example2Messages {
  def main(args: Array[String]) {
    val server = actorOf(new Creator[Actor] {
      def create = new Actor {
        val players = new HashSet[String]
        protected def receive = {
          case Login(name:String) => players.add(name)
          case Attack("darth vader", damage: Int) => println("the sith lord is impervious to pain")
          case Attack(target: String, damage: Int) => println(target + " just took " + damage + " damage.")
        }
      }
    })
    server.start()

    server ! Login("larry")
    server ! Login("darth vader")

    server ! Attack("larry", 123)
    server ! Attack("darth vader", 123)
  }
}