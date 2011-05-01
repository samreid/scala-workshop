/**
 * Copyright 2011, Derek Chen-Becker
 */
package code.comet

import net.liftweb.http.{S, SessionVar, SHtml, CometActor}
import net.liftweb.http.js.{JsCmd, JsCmds}
import xml._
import org.frontierdevelopers.scalaworkshop.day2.session2._
import akka.actor.{Actors, Actor}
import net.liftweb.common.{Full, Box, Empty}
import code.util.{BridgeActor, Controller}

case class State(name : String, messages : List[String], choices : List[Choice])

class GameDisplay extends CometActor {
  private val bridge = Actors.actorOf(classOf[BridgeActor]).start()
  bridge ! this

  // A place to store the current state
  object currentState extends
    SessionVar[Box[State]](Empty)

  def render = currentState.is match {
    case Empty => {
      // We need to prompt the player for their name in order to join
      SHtml.ajaxForm(
        <span>What's your name?</span> ++
        SHtml.text("", login) ++
        <input type="submit" value="Log in" />
      )
    }
    case Full(State(name, messages, choices)) => {
      "#name" #> name &
      "#status *" #> messages.reverse.map{Text(_) ++ <br/>} &
      ".choice" #> (
        choices.flatMap { // Transform each choice into a link with a description
          case choice =>
            SHtml.a(() => perform(choice), <li>{choice.description}</li>)
        }
      )
    }
  }

  def perform (choice : Choice) : JsCmd = {
    currentState.is.foreach {
      state =>
        choice.action match {
          // Special handling for messages 
          case d : Display => choice.agent ! d
          case otherMessage => {
            choice.agent !! ClientChoice(state.name, choice.action) match {
              case Some(p : Prompt) => this ! p
              case other => error(other.toString)
            }
          }
        }
    }
    JsCmds.Noop
  }

  override def mediumPriority = {
    case Display(message) => currentState.foreach {
      state => currentState.set(Full(state.copy(messages = message :: state.messages)))
      partialUpdate(updateMessages())
    }
    case Prompt(message, choices) => currentState.foreach {
      state => currentState.set(Full(state.copy(messages = message :: state.messages,
                                            choices = choices)))
      partialUpdate(updateMessages() & updateChoices())
    }
  }

  def login (name : String) {
    Controller.game !! Join(name, bridge, bridge) match {
      case Some(Prompt(message,choices)) => 
        currentState.set(Full(State(name,message :: Nil,choices)))
        reRender()
      case other => error("Error: " + other)
    }
  }

  def updateMessages() : JsCmd = {
    currentState.is.map {
      state => JsCmds.SetHtml("status", state.messages.reverse.flatMap{Text(_) ++ <br/>})
    } openOr JsCmds.Noop
  }

  def updateChoices() : JsCmd = {
    currentState.is.map {
      state => JsCmds.SetHtml("choices",
        ("#choices ^^" #> "ignore" &
         ".choice" #> (
           state.choices.flatMap {
             case choice =>
               SHtml.a(() => perform(choice), <li>{choice.description}</li>)
           }
         )).apply(defaultHtml)
      )
    } openOr JsCmds.Noop
  }
}
