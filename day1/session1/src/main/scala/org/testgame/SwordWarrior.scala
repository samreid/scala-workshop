package org.testgame

trait Named {
  def name: String
}
trait Described {
  def description: String
}

trait Item extends Named with Described

object Items {
  val HealthPotion = Potion("A health potion", "A large, redish colored potion capable of restoring health to whoever consumes it")
}

case class Potion(name: String, description: String) extends Item

case class Container(items: Map[Item, Int]) {
  def + (item: Item): Container = Container(items.get(item) match {
    case None => items + (item -> 1)
    case Some(count) => items + (item -> (count + 1))
  })

  def - (item: Item): Container = items.get(item) match {
    case None => this

    case Some(count) if (count <= 1) => Container(items - item)

    case Some(count) => Container(items + (item -> (count - 1)))
  }
}
object Container {
  def empty: Container = Container(Map.empty[Item, Int])
}

case class LocationId(name: String) extends Named
case class Location(id: LocationId, description: String, items: Container) extends Named with Described {
  def name: String = id.name
}

object Locations {
  val GrassyFields = Location(LocationId("grassy fields"),
    "You are standing in grassy fields. The hot sun is beating down on you.",
    Container.empty + Items.HealthPotion
  )
}

case class Health(value: Int)
object Health {
  val Min = Health(0)
  val Max = Health(100)
}
sealed trait Character {
  def health: Int
  def inventory: Container
}
case class Player(name: String, health: Health, location: Location, inventory: Container)

case class World(
  player: Player
) {
  def actions: List[Action] = Exit :: Look :: pickupActions

  private def pickupActions: List[Action] = {
    player.location.items.items.map {
      case ((item, count)) => Pickup(item)
    }.toList
  }
}
object World {
  def apply(name: String): World =
    World(Player(
      name      = name,
      health    = Health.Max,
      location  = Locations.GrassyFields,
      inventory = Container.empty
    ))
}

sealed trait Action extends (World => World) with Described

case class Pickup(item: Item) extends Action {
  def description = "Pick up " + item.name

  def apply(old: World): World = {
    import old.player
    import player.inventory
    import player.location

    old.copy(
      player = player.copy(
        inventory = inventory + item,
        location  = location.copy(
          items = location.items - item
        )
      )
    )
  }
}

case object Look extends Action {
  def description = "Look around"

  def apply(old: World): World = {
    println(old.player.location.description)

    old
  }
}

case object Exit extends Action {
  def description = "Exit the game"

  def apply(old: World): World = {
    println("Are you sure you want to exit the game? (y/n)")

    readChar().toLower match {
      case 'y' => System.exit(0)
    }

    old
  }
}

object SwordWarrior {
  def main(args: Array[String]) {
    println("Welcome to SwordWarrioer!")
    println("What is your name? ")

    val name = readLine()

    println("Welcome, " + name)

    val world = World(name)

    run(world)
  }

  def run(world: World): World = {
    println("What would you like to do now? ")

    val actions = world.actions

    actions.zipWithIndex.foreach {
      case (action, index) =>
        println(index + ". " + action.description)
    }

    val newWorld = (readInt() match {
      case choice if (choice < 0 || choice >= actions.length) =>
        println("That is not a choice you have, " + world.player.name)
        world

      case choice =>
        val chosenAction = actions(choice)

        println("You have chosen to: " + chosenAction.description)

        chosenAction(world)
    })

    run(newWorld)
  }
}