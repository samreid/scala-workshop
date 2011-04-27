/**
 * Copyright 2011, Derek Chen-Becker
 */
package code.comet

import net.liftweb.http.{S, SessionVar, SHtml, CometActor}
import net.liftweb.http.js.{JsCmd, JsCmds}
import xml._
import code.util.Controller
import org.frontierdevelopers.scalaworkshop.day2.session2._
import akka.actor.{Actors, Actor}
import net.liftweb.common.{Full, Box, Empty}

case class State(name : String, messages : List[String], choices : List[Choice])

class BridgeActor extends Actor {
  private var target : Option[CometActor] = None
  def receive = {
    case comet : CometActor => target = Some(comet)
    case msg => target.foreach(_ ! msg)
  }
}

class GameDisplay extends CometActor {
  // A place to store the current state
  object currentStateVar extends
    SessionVar[Box[State]](Empty)
  def currentState = currentStateVar.is

  def render = currentState match {
    case Empty => {
      // We need to prompt the player for their name in order to join
      SHtml.ajaxForm(
        <span>What's your name?</span> ++
        SHtml.text("", login) ++
        <input type="submit" value="Log in" />
      )
    }
    case Full(state @ State(name, messages, choices)) => {
      "#status *" #> messages.reverse.map{Text(_) ++ <br/>} &
      ".choice" #> (
        choices.flatMap {
          case choice =>
            SHtml.a(() => perform(choice), <li>{choice.description}</li>)
        }
      )
    }
  }

  def perform (choice : Choice) = currentState.map {
    state =>
      choice.action match {
        case d : Display => choice.agent ! d
        case otherMessage => {
          choice.agent !! ClientChoice(state.name, choice.action) match {
            case Some(p : Prompt) => this ! p
            case other => error(other.toString)
          }
        }
      }

    JsCmds.Noop
  } openOr JsCmds.Noop

  override def mediumPriority = {
    case Display(message) => currentState.foreach {
      state => currentStateVar(Full(state.copy(messages = message :: state.messages)))
      updateMessages()
    }
    case Prompt(message, choices) => currentState.foreach {
      state => currentStateVar(Full(state.copy(messages = message :: state.messages,
                                          choices = choices)))
      updateMessages()
      updateChoices()
    }
  }

  def login (name : String) {
    val bridge = Actors.actorOf(classOf[BridgeActor]).start()
    bridge ! this

    Controller.game !! Join(name, bridge, bridge) match {
      case Some(Prompt(message,choices)) => currentStateVar(Full(State(name,message :: Nil,choices))); reRender()
      case other => error("Error: " + other)
    }

  }

  def updateMessages() {
    currentState.foreach {
      state =>
        partialUpdate(JsCmds.SetHtml("status", state.messages.reverse.flatMap{Text(_) ++ <br/>}))
    }
  }

  def updateChoices() {
    currentState.foreach {
      state =>
      partialUpdate(JsCmds.SetHtml("choices",
        <ul>
          { state.choices.flatMap {
          case choice =>
            SHtml.a(() => perform(choice), <li>{choice.description}</li>)
          } }
        </ul>
      ))
    }
  }
}