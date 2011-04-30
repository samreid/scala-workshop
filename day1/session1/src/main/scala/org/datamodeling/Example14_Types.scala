package org.datamodeling

object Example14_Types {
  def main(args: Array[String]) {

    object HugeUglyMonster extends Monster("Troll", "Glurk") with Huge with Ugly

    val hum: HugeUglyMonster.type = HugeUglyMonster
    val mhu: Monster with Huge with Ugly = HugeUglyMonster
    val monster: Monster = HugeUglyMonster //or Mr. Wiggles
    val huge: Huge = HugeUglyMonster //or Mr. Wiggles
    val ugly: Ugly = HugeUglyMonster //but not Mr. Wiggles!

  }
}