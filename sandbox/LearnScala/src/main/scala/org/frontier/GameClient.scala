/**
 * Copyright 2011, Derek Chen-Becker
 */

package org.frontier

import annotation.tailrec
import akka.actor.{ScalaActorRef, Actor, ActorRef}
import Actor._


object GameClient {
  def main (args : Array[String]) {
    remote.start()

    val (name, prompt) = makeCharacter()
    loopGame(name, prompt)
  }

  implicit def nameToActorRef (name : String) : ScalaActorRef =
    remote.actorFor(name, "localhost", 9292)

  val game = nameToActorRef("game")

  @tailrec
  def makeCharacter() : (String,Prompt) = {
    println("What is your name?")

    val name = readLine()
    (game !! Join(name)) match {
      case Some(prompt : Prompt) => (name,prompt)
      case Some(message : String) => println(message); makeCharacter()
      case _ => makeCharacter()
    }
  }

  def shutdown() {
    remote.shutdown
    exit(0)
  }

  @tailrec
  def loopGame(name : String, prompt : Prompt) {
    println(prompt.message)
    println("You may:")

    prompt.choices.zipWithIndex.foreach {
      case (choice,index) => println("%d. %s".format(index,choice.description))
    }
    println("x. Exit")

    readLine() match {
      case "x" => println("Goodbye!"); shutdown(); // only here to make it tail-recursive
      case pick => {
        try {
          val choice = prompt.choices(pick.toInt)
          (choice.agent !! choice.message) match {
            case Some(newPrompt : Prompt) => loopGame(name, newPrompt)
            case Some(Info(message)) => println(message)
            case _ => println("Invalid response from server, try again")
          }
        } catch {
          case ex if ex.isInstanceOf[NumberFormatException] || ex.isInstanceOf[IndexOutOfBoundsException] => {
            println("Invalid choice!")
          }
        }
      }
    }

     loopGame(name, prompt)
  }
}