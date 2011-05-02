package org.frontierdevelopers.scalaworkshop.day2.session2

import akka.actor.{ActorRef, Actor}

/**
 * This actor is our game engine. It's responsible for
 * receiving messages from players, processing them, and sending
 * result messages back to one or more players. It's responsible
 * for the entire state of the game.
 */
class GameActor extends Actor {
  /**
   * The current state of the game. The "private[this]" modifier
   * makes the variable private to the <b>instance</b> of GameActor,
   * not just private to instances of the GameActor class.
   */
  private[this] var currentState = new GameState(GameMap.initialMap)

  /**
   * This computes the current choices available to a given player,
   * identified by their name, based on a given state.
   */
  def currentChoices(name : String, state : GameState) : List[Choice] = {
    val currentLocation = state.players(name).location

    // Computes movement options based on current location.
    val travel : List[Choice] =
      for (link <- state.map.links(currentLocation))
        yield Choice("Move to " + link.description, self, Move(link))


    // Computes chat options based on which other players are in the same location
    val chat : List[Choice] =
      for ((otherName,otherPlayer) <- state.players.toList
           if otherName != name && otherPlayer.location == currentLocation)
        yield Choice("Chat with " + otherName, otherPlayer.display, Display(otherName))

    // The current choices are the concatenation of movement and chat choices
    travel ::: chat
  }

  /**
   * This is a utility method to encapsulate the logic of notifying other players
   * when a player arrives or leaves their location. The logic in this method is
   * defined for after the player moves since the other players' current choices
   * are dependent on the game state. Note that the from is optional because
   * when players first enter the game they essentially arrive from nowhere
   */
  def notifyLocationChange(currentPlayer: Player, from : Option[Site], to: Site, state : GameState) {
    val name = currentPlayer.name

    /* This is a local utility function we can use to send messages uniformly. By
     * providing two argument lists we can construct a single-argument function
     * based on the message and then pass that function to our foreach calls
     * later in the notifyLocationChange method.
     *
     * Basically what we're doing here is sending another player a new set
     * of choices based on our current player arriving at or leaving a
     * location.
     */
    def notifyOther(msg : String)(otherPlayer : Player) = {
      otherPlayer.input ! Prompt(otherPlayer.toString, Some(msg), currentChoices(otherPlayer.name, state))
    }

    // We first notify others in the "from" location that the player has left
    from.foreach {
      former => currentState.players.values.filter(p => p.name != name &&
                                                   p.location == former).foreach(notifyOther(name + " has left"))
    }

    // Then we notify those in the "to" location that the player has arrived
    currentState.players.values.filter(p => p.name != name &&
                                       p.location == to).foreach(notifyOther(name + " has arrived"))
  }

  /**
   * We delegate to this partial function to allow the compiler to
   * utilize the fact that we've sealed Action. We return a tuple
   * of the new game state as well as an optional message
   * that may be sent to the client's display actor.
   */
  val processAction : PartialFunction[(Player,Action),(GameState,Option[String])] = {
    case (currentPlayer,Move(to)) => {
      val formerLocation = currentPlayer.location
      val newState = 
        currentState.copy(players = currentState.players + (currentPlayer.name -> currentPlayer.copy(location = to)))

      notifyLocationChange(currentPlayer, Some(formerLocation), to, newState)

      /* A nested copy can be used to modify our player's location
       * and the global game state, but you can see how this could
       * start to get ugly if we go deeper. */
      (newState,
       Some("You are in a " +  to.description))
    }
  }

  /**
   * The receive method is Akka's main actor processing loop. It's
   * typed as PartialFunction[Any,Unit], so we delegate to
   * processAction in order to get some additional compiler enforcement.
   */
  def receive = {
    case Join(name,input,display) => {
      val player = Player(name, GameMap.townSquare, display, input)
      currentState = currentState.copy(players = currentState.players + (name -> player))
      notifyLocationChange(player, None, player.location, currentState)
      self.reply_?(Prompt(player.toString, Some("Welcome!\nYou are in " + player.location.description), currentChoices(name,currentState)))
    }
    case choice @ ClientChoice(playerName, action) => {
      /* The "get" operation on the players Map returns an Option[Player],
       * which allows us to further chain operations (mapping via processAction)
       * and consolidate several actions that only occur if the player name
       * is actually registered. */
      currentState.players.get(playerName).map(processAction(_,action)).foreach {
        case (newState,message) => {
          /* At this point we know that the player exists because we're inside
           * the foreach */
          val player = newState.players(playerName)

          // Make sure we record the global state change
          currentState = newState
          
          // We can respond with the new status and choices
          self.reply_?(Prompt(player.toString, message, currentChoices(playerName, currentState)))
        }
      }
    }
  }
}

