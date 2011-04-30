import sbt._

class Dungeon(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {
  override def mainClass = Some("dungeon.LootTheLand")
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.7.2" % "test"
  val check = "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
}

// vim: set ts=4 sw=4 et:
