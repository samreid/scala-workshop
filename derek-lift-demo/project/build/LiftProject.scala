import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val liftVersion = "2.3"

  val akka_repo = "Akka Maven Repository" at "http://akka.io/repository"
  val jboss_repo = "JBoss Maven Repository" at "http://repository.jboss.org/nexus/content/groups/public/"
  val guiceyfruit_repo = "GuiceyFruit repo" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"

  val akka = "se.scalablesolutions.akka" % "akka-actor" % "1.0"
  val akkaRemote = "se.scalablesolutions.akka" % "akka-remote" % "1.0"

  val gameCode = "org.fdgame" %% "workshop-d2s2" % "1.0"

  // uncomment the following if you want to use the snapshot repo
  //  val scalatoolsSnapshot = ScalaToolsSnapshots

  // If you're using JRebel for Lift development, uncomment
  // this line
  // override def scanDirectories = Nil

  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test",
    "junit" % "junit" % "4.5" % "test",
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    "org.scala-tools.testing" %% "specs" % "1.6.6" % "test"
  ) ++ super.libraryDependencies
}
