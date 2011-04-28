/**
 * Copyright 2011, Derek Chen-Becker
 */
package code.util

import akka.actor.Actor
import net.liftweb.http.CometActor

class BridgeActor extends Actor {
  private var target : Option[CometActor] = None
  def receive = {
    case comet : CometActor => target = Some(comet)
    case msg => target.foreach(_ ! msg)
  }
}