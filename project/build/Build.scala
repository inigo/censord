import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

  // For use with JRebel 
  override def scanDirectories = Nil
}
