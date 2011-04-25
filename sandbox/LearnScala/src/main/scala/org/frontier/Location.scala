/**
 * Copyright 2011, Derek Chen-Becker
 */

package org.frontier

trait Location extends HasInventory {
  val name : String
  val description : String
}

case class GameLocation (name : String, description : String) extends Location

object Nowhere extends Location {
  val name = "Nowhere"
  val description = "Nothing, really"
}

object Expended extends Location {
  val name = "Used Up"
  val description = "Where expendable things go after they're used"
}