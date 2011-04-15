/**
 * Copyright 2011, Derek Chen-Becker
 */

package org.frontier


class Creature (val species : String,
                val strength : Int = 10,
                private var _health : Double = 50,
                val attack : Double = 5,
                val friendly : Boolean = false) {
  val maxHealth = _health

  def damage (amount : Double) : Unit = {
    health = scala.math.max(0, health - amount)
  }

  def heal(amount : Double) {
    health = health + amount
  }

  def heal() : Unit = heal(5) // default amount

  override def toString = // This is a special method
    "%s (%.2f/%.2f)".format(species, health, maxHealth)

  def health = _health
  def health_= (n : Double) =
    if (n >= 0 && n <= maxHealth) _health = n
}



