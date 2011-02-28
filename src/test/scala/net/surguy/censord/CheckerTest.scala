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
      ensureOnly("fish,red")
      new Checker(Phrase.findAll).checkText("one fish two fish red fish blue fish") must haveClass[Rejected]
    }
    "return true if there are no unacceptable words" in {
      ensureOnly("fish,red")
      new Checker(Phrase.findAll).checkText("the cat in the hat") must haveClass[Accepted]
    }
  }
  "finding partial words" should {
    "accept the text if a word begins with a forbidden term but stemming is disabled" in {
      ensureOnly("fish")
      new Checker(Phrase.findAll).checkText("going fishing now") must haveClass[Accepted]
    }
    "reject the text if a word begins with a forbidden term but stemming is enabled" in {
      ensureOnly("fish*")
      new Checker(Phrase.findAll).checkText("going fishing now") must haveClass[Rejected]
      // beLike { case Accepted => true }
    }
    "accept the text if the word is embedded within other words and stemming is enabled" in {
      ensureOnly("ish*")
      new Checker(Phrase.findAll).checkText("going fishing now") must haveClass[Accepted]
    }
    "accept the text if the word is embedded within other words and stemming is disabled" in {
      ensureOnly("ish")
      new Checker(Phrase.findAll).checkText("going fishing now") must haveClass[Accepted]
    }
  }

  private def ensureOnly(words: String) = { deleteAll(); Phrase.createMultiplePhrases(words) }

  private def deleteAll() = {
    Phrase.findAll.foreach( _.delete_! )
    Phrase.findAll.size must beEqualTo(0)
  }

}