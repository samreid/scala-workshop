/**
 * Copyright 2011, Derek Chen-Becker
 */

package org.frontier

import java.io.Serializable


class Character(species : String,
                val name : String,
                strength : Int = 10,
                val intelligence : Int = 10,
                health : Double = 50,
                attack : Double = 5,
                friendly : Boolean = true)
    extends Creature(species, strength, health, attack, friendly) with HasInventory with Serializable {
  override def toString = "%s (%.2f/%.2f)".format(name, health, maxHealth)

  override def equals(other: Any) = other match {
    case that : Character => this.name == that.name && this.species == that.species
    case _ => false
  }

  override def hashCode() = (species + ":" + name).hashCode()
}