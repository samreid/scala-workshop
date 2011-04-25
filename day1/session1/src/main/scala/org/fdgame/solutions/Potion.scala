/**
 * Copyright 2011, Derek Chen-Becker
 */
package org.fdgame.solutions


case class Potion(name : String, color : String, healing : Int) extends Item {
  val weight = 0.25
  val description = "A %s potion".format(color)
}
