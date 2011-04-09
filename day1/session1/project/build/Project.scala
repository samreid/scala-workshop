import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {
  val scala_swing = "org.scala-lang" % "scala-swing" % "2.8.1"
}

// vim: set ts=4 sw=4 et:
