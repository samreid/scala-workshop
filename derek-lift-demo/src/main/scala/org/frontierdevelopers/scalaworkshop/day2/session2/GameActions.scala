package org.frontierdevelopers.scalaworkshop.day2.session2


/**
 * This trait will allow us to constrain what types of actions
 * the client can take. Because it is sealed, we can only define
 * client actions within <b>This</b> source file. Additionally,
 * the compiler can then deduce the exact set of actions and
 * warn us if we perform a pattern match on Action and miss
 * a case.
 */
sealed trait Action

/**
 * Represents movement of a given player to a new location
 */
case class Move(to : Site) extends Action


/**
 * Represents displaying a message to the client. In this case
 * we constrain it to type Action because we want to allow
 * the client to send messages to other clients (Chat), but we
 * can also use it to send async messages from the game engine
 * to the client.
 */
case class Display(message : String) extends Action
