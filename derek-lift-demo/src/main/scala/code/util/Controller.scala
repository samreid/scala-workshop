/**
 * Copyright 2011, Derek Chen-Becker
 */
package code.util

import akka.actor.Actors
import org.frontierdevelopers.scalaworkshop.day2.session2.GameActor


object Controller {
  val game = Actors.actorOf(classOf[GameActor]).start()
}