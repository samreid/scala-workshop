package dung.eon
import scala.util.Random

sealed trait GameState
case object Finished extends GameState
case class Running() extends GameState

trait Dungeon {
  def run(state: GameState): Unit = nextState(state) match {
    case Finished => ()
    case next => run(next)
  }

  def initialState: GameState
  def nextState(state: GameState): GameState
}

object Main extends Dungeon {
  def initialState: GameState = Running()
  def nextState(state: GameState): GameState = Finished

  def main(argv: Array[String]) {
    run(initialState)
  }
}

sealed trait Magic { def attack: Option[Int] }
case class Fire(attack: Option[Int], range: Int) extends Magic

sealed trait Alignment
object Alignment {
  case object Lawful  extends Alignment
  case object Neutral extends Alignment
  case object Chaotic extends Alignment
}

sealed trait Character {
  def alignment: Alignment
  def attack: Int
  def defense: Int
}


case class NPC(alignment: Alignment, attack: Int, defense: Int) extends Character
case class Monster(alignment: Alignment, attack: Int, defense: Int) extends Character

trait EncounterGenerator {
  def encounter: List[Character]
}

case object Abandoned extends EncounterGenerator {
  val encounter = Nil
}

class Item(name: String, magic: Option[Magic])

case class Location(name: String, items: List[Item], encounterGenerator: EncounterGenerator)
case class Exit(loc: Location, message: String)

class DungeonMap(map: Map[Location, Set[Exit]]) {
  def exits(loc: Location): Set[Exit] = map(loc)
}

object DungeonMap {
  val defaultLocations = List(
    Location("The Inn", Nil, Abandoned),
    Location("The Forest", Nil, Abandoned),
    Location("The Cave", Nil, Abandoned),
    Location("The Spring", Nil, Abandoned),
    Location("The Spring", Nil, Abandoned)
  )

  def generate(locations: List[Location]): DungeonMap = {
    // Randomly generate a pair of directions that will be used for the "to" and "from"
    // instructions to get between two locations.
    def dirs = {
      val pair = if (Random.nextBoolean) ("East", "West") else ("North", "South") 
      if (Random.nextBoolean) pair.swap else pair
    }
      
    // add exits between a pair of locations to the map
    def addExits(a: Location, b: Location, locs: Map[Location, Set[Exit]]) = {
      val (from, to) = dirs
      Map(a -> Exit(b, "Go " + from), b -> Exit(a, "Go " + to)).foldLeft(locs) {
        case (m, (l, e)) => m + (l -> (m.getOrElse(l, Set()) + e))
      }
    }
    
    // Randomly arrange the locations, then using a sliding window 4 elements wide,
    // add mutual entrances and exits between locations. The topology will likely end
    // up pretty funky, but that's okay.
    new DungeonMap(
      Random.shuffle(locations).sliding(4).foldLeft(Map.empty[Location, Set[Exit]]) {
        case (m, List(a, b, c, d)) => addExits(a, b, addExits(a, c, addExits(a, d, m)))
      }
    )
  }
}

// vim: set ts=4 sw=4 et:
