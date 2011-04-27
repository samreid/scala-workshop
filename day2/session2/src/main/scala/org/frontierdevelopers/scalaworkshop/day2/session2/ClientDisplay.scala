/**
 * Copyright 2011, Derek Chen-Becker
 */
package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor

class ClientDisplay extends Actor {
  def receive = {
    case Display(message) => println(message)
  }
}