/**
 * Copyright 2011, Derek Chen-Becker
 */
package org.fdgame.solutions

case class Character(name : String,
                     var health : Int,
                     var items : List[Item] = Nil,
                     var armor : Option[Armor] = None,
                     var weapon : Option[Weapon] = None)