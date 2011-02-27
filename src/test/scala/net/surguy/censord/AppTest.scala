package net.surguy.censord

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import org.specs.SpecificationWithJUnit
import scala.collection.JavaConverters._
import java.io.{FileInputStream, File}

/**
 * Application level tests.
 */
class AppTest extends SpecificationWithJUnit {

  "every html and xml file in src/main/webapp" should {
    "be well formed XML" in {
      malformedFiles(new File("src/main/webapp")) must beEmpty
    }
  }

  private def isXhtml(file: String) = file.endsWith(".html") || file.endsWith(".htm") || file.endsWith(".xhtml")

  private def malformedFiles(file: File): List[File] = file match {
   case f if f.isDirectory => f.listFiles.map(malformedFiles).toList.flatten
   case f if f.isFile && f.exists && isXhtml(f.getName) =>
     PCDataXmlParser(new FileInputStream(f)) match {
       case Full(_) => List()
       case _ => List(file)
     }
   case _ => List()
  }

}
