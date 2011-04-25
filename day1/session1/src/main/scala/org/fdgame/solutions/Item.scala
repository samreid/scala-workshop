/**
 * Copyright 2011, Derek Chen-Becker
 */
package org.fdgame.solutions

/**
 * The base trait for items.
 */
trait Item {
  val name : String
  val description : String
  def weight : Double
}