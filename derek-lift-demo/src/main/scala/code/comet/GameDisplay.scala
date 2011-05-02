package code.comet

import net.liftweb.http.{S, SessionVar, SHtml, CometActor}
import net.liftweb.http.js.{JsCmd, JsCmds}
import xml._
import org.frontierdevelopers.scalaworkshop.day2.session2._
import akka.actor.{Actors, Actor}
import net.liftweb.common.{Full, Box, Empty}
import code.util.{BridgeActor, Controller}

/**
 * This case class will contain our client state
 */
case class ClientState(name : String, status : String, messages : List[String], choices : List[Choice])

/**
 * This is our Comet actor. It's responsible for rendering our current state
 * as well as any new state returned from the GameActor.
 */
class GameDisplay extends CometActor {
  // A bridge between the Lift and Akka actor libraries
  private val bridge = Actors.actorOf(classOf[BridgeActor]).start()
  bridge ! this

  /* A SessionVar represents a variable held by the
   * servlet Session. It provides some nice wrapper functionality,
   * including a default value and some transform/set operations.
   * Box is a Lift implementation of Option, with Empty being
   * the analogue of None. */
  object currentState extends
    SessionVar[Box[ClientState]](Empty)

  /* The render method is responsible for performing a complete render
   * of this component. You can also perform piecemeal updates via
   * the partialUpdate method (shown later). */
  def render = currentState.is match {
    case Empty => {
      /* We need to prompt the player for their name in order to join
       * the game. The ajaxForm method wraps a regular HTML form
       * (specified here directly with Scala's XML literals) and processes
       * the form as an AJAX submission. */
      SHtml.ajaxForm(
        <span>What's your name?</span> ++
        /* SHtml.text generates a text input that invokes a Scala
         * callback (in this case, the login method) with the text
         * it contains when the form is submitted. */
        SHtml.text("", login) ++ 
        <input type="submit" value="Log in" />
      )
    }
    case Full(state @ ClientState(name, status, messages, choices)) => {
      /* When we have state to render, utilize Lift's
       * CSS binding Domain-Specific Language (DSL) to
       * process the template we were given. More on CSS Bindings
       * can be found here:
       *
       * http://www.assembla.com/wiki/show/liftweb/Binding_via_CSS_Selectors
       */
      "#status *" #> status &
      "#messages *" #> messages.reverse.map{Text(_) ++ <br/>} &
      ".choice" #> generateChoices(state)
    }
  }

  /**
   * This defines an AJAX callback for executing a selected choice.
   * It returns a JsCmd, which is part of Lift's abstraction for
   * dealing with JavaScript.
   */
  def perform (choice : Choice)() : JsCmd = {
    /* The "is" accesses the current value of our SessionVar, and Box
     * contains a foreach (and map, etc) just like Option. */
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

  /**
   * Lift's actors, unlike Akka, define three different message handlers that are
   * tried in order (high, medium, low). However, like Akka, Lift uses a partial
   * function for the actual message handling.
   */
  override def mediumPriority = {
    case Display(message) => currentState.foreach {
      state => {
        currentState.set(Full(state.copy(messages = message :: state.messages)))
        partialUpdate(updateMessages())
      }
    }
    case Prompt(newStatus, message, newChoices) => currentState.foreach {
      state => {
        // Optionally prepend the provided message
        val newMessages = message.map(_ :: state.messages) getOrElse state.messages

        currentState.set(Full(state.copy(status = newStatus,
                                         messages = newMessages,
                                         choices = newChoices)))
        partialUpdate(updateMessages() & updateChoices())
      }
    }
  }

  /**
   * This defines our callback when the user submits their name
   * to join the game. We need to submit to the GameActor and wait
   * to make sure our user name was accepted. If we get any errors
   * we notify the user.
   */
  def login (name : String) {
    Controller.game !! Join(name, bridge, bridge) match {
      case Some(Prompt(status,message,choices)) => 
        currentState.set(Full(ClientState(name,status,message.map(List(_)) getOrElse Nil,choices)))
      case other => error("Error: " + other)
    }
    reRender()
  }

  /**
   * A utiltiy method to generate the JavaScript necessary to only update
   * the "messages" div.
   */
  def updateMessages() : JsCmd = {
    currentState.is.map {
      state => JsCmds.SetHtml("messages", state.messages.reverse.flatMap{Text(_) ++ <br/>})
    } openOr JsCmds.Noop
  }

  /**
   * A utility method to generate the JavaScript necessary to only update
   * the "choices" div
   */
  def updateChoices() : JsCmd = {
    currentState.is.map {
      state => JsCmds.SetHtml("choices",
        ("#choices ^^" #> "ignore" &
         ".choice" #> generateChoices(state)).apply(defaultHtml)
      ) &
      JsCmds.SetHtml("status", Text(state.status))
    } openOr JsCmds.Noop
  }

  /**
   * Because we need to generate choices in both our main render and our
   * updateChoices methods, we refactor out the common generation here.
   */
  def generateChoices(state : ClientState) : NodeSeq = state.choices.flatMap {
    /* Special handling for chat messages. Here we create a new form
     * that allows us to customize our message */
    case Choice(description, agent, Display(otherPlayer)) => {
      SHtml.ajaxForm(
        <li>Say "{ SHtml.text("hi", message => agent ! Display(state.name + " says: " + message)) }" to {otherPlayer}
        <input type="submit" value="Send!" /></li>
      )
    }
    case choice =>
      SHtml.a(() => perform(choice), <li>{choice.description}</li>)
  }
}
