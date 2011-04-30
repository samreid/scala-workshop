package dungeon
import scala.util.Random

sealed trait GameState
case object Finished extends GameState
case class Running(player: Player, playerLoc: Location, loot: Map[Location, List[Item]]) extends GameState

trait Dungeon {
  def run(state: GameState): Unit = nextState(state) match {
    case Finished => ()
    case next => run(next)
  }

  def nextState(state: GameState): GameState
}

object LootTheLand extends Dungeon {
  def main(argv: Array[String]) {
    print("Greetings, brave adventurer! What is your name? > ")
    run(
      Running(
        Player(readLine(), Neutral, 10 + Random.nextInt(10), 20 + Random.nextInt(20)),
        inn,
        Map(cave -> List(FlamingSword), forest -> List(Club))
      )
    )
  }

  val inn = Location("The Inn", FriendlyPeople)
  val cave = Location("The Cave", Monsters)
  val forest = Location("The Forest", Abandoned)
  val map = DungeonMap.generate(
    inn, cave, forest,
    Location("The Spring", Abandoned),
    Location("The Mountain", Monsters)
  )

  def nextState(state: GameState): GameState =  state match {
    case Running(player, loc, loot) =>
      println(describeLocation(loc))
      for (items <- loot.get(loc)) {
        println(describeLoot(loot(loc)))
      }

      val currentChoices = choices(loc, loot.get(loc).toList.flatten)
      currentChoices.zipWithIndex.foreach {
        case (c, i) => println(i + ". " + c.description)
      }

      currentChoices(readLine.toInt) match {
        case PathChoice(path) => Running(player, path.loc, loot)
        case PickUpChoice(item) => Running(
          player.copy(inventory = item +: player.inventory),
          loc,
          removeItem(loc, item, loot)
        )
      }

    case Finished => Finished
  }

  private def describeLocation(loc: Location) = {
    val paths = map.paths(loc).map(_.direction)

    "You are in " + loc.name + ". There are paths leading to the " +
    (paths.init.mkString(", ") + " and " + paths.last) + "."
  }

  private def describeLoot(items: List[Item]) = items match {
    case Nil => "There's nothing here"
    case i :: Nil => "You see a " + i + " lying on the ground."
    case xs => "You see " + xs.init.mkString(", ") + " and a " + xs.last + " lying on the ground."
  }

  private def choices(loc: Location, loot: List[Item]) = {
    (loot.map(PickUpChoice) ++ map.paths(loc).map(PathChoice)).toList
  }

  private def removeItem(loc: Location, item: Item, loot: Map[Location, List[Item]]) = {
    loot(loc).filter(_ != item) match {
      case Nil => loot - loc
      case xs => loot + (loc -> xs)
    }
  }
}

sealed trait Choice {
  def description: String
}
case class PathChoice(path: Path) extends Choice {
  def description = "Go " + path.direction
}

case class PickUpChoice(item: Item) extends Choice {
  def description = "Pick up the " + item.name
}

sealed trait Magic { def attack: Option[Int] }
case class Fire(attack: Option[Int], range: Int) extends Magic

sealed trait Alignment
case object Lawful  extends Alignment
case object Neutral extends Alignment
case object Chaotic extends Alignment

sealed trait Character {
  def alignment: Alignment
  def attack: Int
  def defense: Int
}

case class Player(name: String, alignment: Alignment, attack: Int, defense: Int, inventory: Inventory = new Inventory()) extends Character
case class NPC(name: String, alignment: Alignment, attack: Int, defense: Int) extends Character
case class Monster(species: String, alignment: Alignment, attack: Int, defense: Int) extends Character

trait EncounterGenerator {
  def encounter: List[Character]
}

case object Abandoned extends EncounterGenerator {
  val encounter = Nil
}

object FriendlyPeople extends EncounterGenerator {
  def encounter = List(
    NPC("The Innkeeper", Lawful, 3, 10),
    NPC("The Rogue", Chaotic, 6, 28)
  )
}

object Monsters extends EncounterGenerator {
  def encounter = List(
    Monster("Goblin", Chaotic, 2, 5),
    Monster("Orc", Chaotic, 7, 19),
    Monster("Dragon", Chaotic, 25, 40)
  )
}

case class Item(
  name: String,
  cursed: Boolean,
  weight: Int = 1,
  durability: Int = 2,
  attack: Int = 0,
  magic:  Option[Magic] = None,
  priorOwners: List[Character] = Nil
)

object Club extends Item("Club", false, 3, 15, 2)
object FlamingSword extends Item("Flaming Sword", false, 3, 100, 10, Some(Fire(Some(30), 2)))

class Inventory(val items: List[Item] = Nil) {
  def +(item: Item) = new Inventory(items :+ item)

  def +:(item: Item) = new Inventory(item :: items)

  def totalWeight: Int = items.map(_.weight).sum

  def findByName(s: String): Option[Item] = {
    items.find(_.name == s)
  }

  def damage(amount: Int) = {
    val inflict = (item: Item) => {
      item.copy(durability = item.durability - amount)
    }

    new Inventory(
      items.map(inflict).filter(_.durability <= 0)
    )
  }
}

case class Location(name: String, encounterGenerator: EncounterGenerator)
case class Path(loc: Location, direction: String)

class DungeonMap(map: Map[Location, Set[Path]]) {
  def paths(loc: Location): Set[Path] = map(loc)
}

object DungeonMap {
  def generate(locations: Location*): DungeonMap = {
    // Randomly generate a pair of directions that will be used for the "to" and "from"
    // instructions to get between two locations.
    def dirs = {
      val pair = if (Random.nextBoolean) ("East", "West") else ("North", "South") 
      if (Random.nextBoolean) pair.swap else pair
    }
      
    // add paths between a pair of locations to the map
    def addPaths(a: Location, b: Location, locs: Map[Location, Set[Path]]) = {
      val (from, to) = dirs
      Map(a -> Path(b, from), b -> Path(a, to)).foldLeft(locs) {
        case (m, (l, e)) => m + (l -> (m.getOrElse(l, Set()) + e))
      }
    }
    
    // Randomly arrange the locations, then using a sliding window 4 elements wide,
    // add mutual entrances and paths between locations. The topology will likely end
    // up pretty funky, but that's okay.
    new DungeonMap(
      Random.shuffle(locations.toList).sliding(4).foldLeft(Map.empty[Location, Set[Path]]) {
        case (m, List(a, b, c, d)) => addPaths(a, b, addPaths(a, c, addPaths(a, d, m)))
      }
    )
  }
}

// vim: set ts=4 sw=4 et:
