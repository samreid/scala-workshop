/**
 * Created by IntelliJ IDEA.
 * User: knuttycombe
 * Date: 4/27/11
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */

class Item(val name: String, val cursed: Boolean)

object Items {

val hat: Item = new Item("Hat", true)

val message: String = if (hat.cursed) {
  "That hurts to touch."
} else {
  "Seems okay."
}

val item: Item = new Item("Hat", true)]

val msg = if (item.cursed) "Ouch!" else "What?"

//Multiline works okay too:

val msg = if (item.cursed) "Ouch!"
          else "What?"


val message: Any = if (item.cursed) {
  "This thing hurts!"
}

var message:  String = _
var message2: String = _

if (item.cursed) {
  message  = "That hurts to touch."
  message2 = "You think it might be cursed?"
} else {
  message  = "Seems okay."
  message2 = "Doesn't look like it'll bite."
}

}

object Functions {

((i: Int) => i + 1)


val inc  = (i: Int) => i + 1
val inc2 = (_: Int) + 1


val add  = (i: Int, j: Int) => i + j
val add2 = (_: Int) + (_: Int)
}

object Functions2 {


trait Function1[-A, +B] {
  def apply(a: A): B
}

val add1 = (i1: Int, i2: Int) => i1 + i2
val add2 = new Function2[Int, Int, Int] {
  def apply(i1: Int, i2: Int): Int = i1 + i2
}

class Item(
  val name: String,
  val weight: Int,
  val cursed: Boolean = false,
  val durability: Int = 2,
  val attack: Int = 0,
  val magic:  Option[Magic] = None
)

object Item {
  val show: Item => String = (item: Item) =>
    item.name + " (weight: "+item.weight+")"

  val showTruth = (item: Item) => {
    if (item.cursed) "CURSED " + show(item)
    else show(item)
  }
}

object WandOfWonder extends Item(
  "Wand of Wonder", 2
)

// Explicit call to apply
Item.show.apply(WandOfWonder)

// The compiler will insert .apply for us
Item.show(WandOfWonder)
}