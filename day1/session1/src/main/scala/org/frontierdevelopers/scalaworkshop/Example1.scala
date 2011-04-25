package org.frontierdevelopers.scalaworkshop

case class Animal(name: String)

class Warrior1 {
  val pet: Animal = null
}

class Warrior2 {
  val pet: Option[Animal] = None
}

object Example1 {
  def main(args: Array[String]) {
    println(new Warrior1().pet)
    println(new Warrior1().pet.name) //Null pointer exception
  }
}

object Example2 {
  def main(args: Array[String]) {
    println(new Warrior2().pet)
    //    println(new Warrior2().pet.name) //Will not compile
    println(new Warrior2().pet match {
              case Some(a: Animal) => a.name
              case None => "No animal yet"
            })
    println(new Warrior1().pet)
  }
}