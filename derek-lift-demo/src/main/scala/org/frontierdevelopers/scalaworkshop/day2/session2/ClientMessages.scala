package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.ActorRef

/**
 * This message is what the client sends to the game engine to
 * join into the game.
 *
 * @param name The desired name of the player
 * @param input An actor that we can send Prompt messages to in order
 * to tell the client what actions they may perform
 * @param display An actor that we can send Display messages to in order
 * to have the client display a new message
 */
case class Join(name : String, input : ActorRef, display : ActorRef)

/**
 * This message allows us to inform the client of a current status
 * and a List of possible choices based on their current state
 */
case class Prompt(status : String, message: Option[String], choices : List[Choice])

/**
 * This is not a message itself, but is a component of the Prompt
 * message. It defines a particular choice a player may make, with
 * a description as well as a reference to the actor that will process
 * the action and the action message itself that will be sent to
 * that actor.
 *
 * In the general case this would allow us to distribute the processing of
 * actions to multiple actors, but since we only have a single game engine
 * actor in this case, the primary purpose is to allow clients to send
 * messages to one another.
 */
case class Choice(description : String, agent : ActorRef, action : Action)

/**
 * This message is what the client sends to a particular Choice's
 * agent informing them of the action they want to take. Additionally,
 * it includes their player name so that the processing actor can
 * determine who is taking the action.
 */
case class ClientChoice(player : String, action : Action)
