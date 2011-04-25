/**
 * Copyright 2011, Derek Chen-Becker
 */
package org.fdgame.solutions


case class Container(name : String, contents : List[Item]) extends Item {
  val description = "A container"
  val weight = contents.map(_.weight).sum
}