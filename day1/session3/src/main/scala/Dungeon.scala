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
    def dirs = {
      val result = if (Random.nextBoolean) ("East", "West") else ("North", "South") 
      if (Random.nextBoolean) result.swap else result
    }

    def addExits(l1: Location, l2: Location, locs: Map[Location, Set[Exit]]) = {
      val (from, to) = dirs
      Map(l1 -> Exit(l2, "Go " + from), l2 -> Exit(l1, "Go " + to)).foldLeft(locs) {
        case (m, (l, e)) => m + (l -> (m.getOrElse(l, Set()) + e))
      }
    }

    new DungeonMap(
      Random.shuffle(locations).sliding(3).foldLeft(Map.empty[Location, Set[Exit]]) {
        case (m, List(a, b, c, d)) => addExits(a, b, addExits(a, c, addExits(a, d, m)))
      }
    )
  }
}

// vim: set ts=4 sw=4 et:
