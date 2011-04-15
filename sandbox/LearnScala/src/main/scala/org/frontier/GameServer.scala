/**
 * Copyright 2011, Derek Chen-Becker
 */
package org.frontier


import akka.actor.Actor._

object GameServer {
  def main (args : Array[String]) {
    remote.start("localhost", 9292)

    println("Game controller started at " + Controllers.game)
    println("Inventory controller started at " + Controllers.inventory)
  }
}