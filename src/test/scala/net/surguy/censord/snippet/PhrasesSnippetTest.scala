package net.surguy.censord.snippet

import org.specs.SpecificationWithJUnit

/**
 * Test disemvowelling.
 *
 * @author Inigo Surguy
 * @created 03/03/2011 20:35
 */

class PhrasesSnippetTest extends SpecificationWithJUnit {

  def disemvowel = new PhrasesSnippet().disemvowel _

  "removing vowels from words" should {
    "turn vowels inside words to stars" in { disemvowel("cat") must beEqualTo("c*t")}
    "turn capital vowels to stars" in { disemvowel("FISHES") must beEqualTo("F*SH*S")}
    // The rules for turning things into stars correctly are complicated... the following doesn't capture them
//    "ignore leading and trailing vowels" in { disemvowel("enforce") must beEqualTo("enf*rce")}
//    "turn adjacent vowels into stars" in { disemvowel("eeyore") must beEqualTo("e*y*r*")}
//    "ignore leading and trailing vowels in words" in { disemvowel("eye igloo") must beEqualTo("eye igl*o")}
  }

}
