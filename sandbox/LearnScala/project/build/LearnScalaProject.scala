import sbt._

class LearnScalaProject(info : ProjectInfo) extends DefaultProject(info) with IdeaProject {
  val akka_repo = "Akka Maven Repository" at "http://akka.io/repository"
  val jboss_repo = "JBoss Maven Repository" at "http://repository.jboss.org/nexus/content/groups/public/"
  val guiceyfruit_repo = "GuiceyFruit repo" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"

  val akka = "se.scalablesolutions.akka" % "akka-actor" % "1.0"
  val akkaRemote = "se.scalablesolutions.akka" % "akka-remote" % "1.0"
}