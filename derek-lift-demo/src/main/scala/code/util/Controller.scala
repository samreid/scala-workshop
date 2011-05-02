package code.util

import akka.actor.Actors
import org.frontierdevelopers.scalaworkshop.day2.session2.GameActor

/**
 * This is just a way to create a singleton game engine
 * for use by our CometDisplay clients.
 */
object Controller {
  val game = Actors.actorOf(classOf[GameActor]).start()
}
