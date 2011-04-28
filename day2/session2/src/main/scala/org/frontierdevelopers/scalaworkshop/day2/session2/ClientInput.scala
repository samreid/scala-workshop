/**
 * Copyright 2011, Derek Chen-Becker
 */
package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.Actor
import annotation.tailrec

class ClientInput extends Actor {
  var name = ""

  def receive = {
    case Prompt(message, choices) => {
      println("\n" + message)

      val choice = readChoice(choices)
      choice.agent ! ClientChoice(name, choice.action)
    }
    case name : String => this.name = name
  }

  @tailrec
  final def readChoice(choices : List[Choice]) : Choice = {
    println("\nYou may:\n")

    choices.zipWithIndex.foreach {
      case (choice : Choice,index : Int) => println((index +1) + ". " + choice.description)
    }

    print("\nChoice: ")

    try {
      return choices(readLine().toInt - 1)
    } catch {
      case e : Exception => println("That isn't a valid choice")
    }

    readChoice(choices)
  }
}