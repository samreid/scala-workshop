/**
 * Copyright 2011, Derek Chen-Becker
 */

package org.frontier


trait Item {
  val name : String
  val description : String
  val weight : Int
}

//trait Inventory {
//  private[this] var currentInventory : List[Item] = Nil
//
//  def inventory = currentInventory
//
//  def add(item : Item) = synchronized {
//    currentInventory = item :: currentInventory
//  }
//
//  def transfer (item : Item, to : Inventory) : Boolean = synchronized {
//    val (items, others) = currentInventory.partition(_ == item)
//    currentInventory = items.drop(1) ::: others
//    items.headOption.map { item => to.add(item); true} getOrElse false
//  }
//}

trait HasInventory