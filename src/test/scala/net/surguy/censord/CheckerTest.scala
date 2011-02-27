package net.surguy.censord

import model.Phrase
import org.specs.SpecificationWithJUnit

/**
 * Test for unacceptable words.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 19:07
 */

class CheckerTest extends SpecificationWithJUnit with DatabaseSetup {

  "checking a text for unacceptable words" should {
    "flag the unacceptable words if present" in {
      deleteAll()
      Phrase.createMultiplePhrases("fish,red")
      new Checker(Phrase.findAll).checkText("one fish two fish red fish blue fish") must beFalse
    }
    "return true if there are no unacceptable words" in {
      deleteAll()
      Phrase.createMultiplePhrases("fish,red")
      new Checker(Phrase.findAll).checkText("the cat in the hat")
    }
  }

  private def deleteAll() = {
    Phrase.findAll.foreach( _.delete_! )
    Phrase.findAll.size must beEqualTo(0)
  }

}