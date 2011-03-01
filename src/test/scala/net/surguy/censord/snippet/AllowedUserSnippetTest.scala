package net.surguy.censord.snippet

import org.specs.SpecificationWithJUnit

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 01/03/2011 20:58
 */

class AllowedUserSnippetTest extends SpecificationWithJUnit {

  def gravatar(s: String) = new AllowedUserSnippet().gravatarUrl(s)

  "creating a gravatar" should {
    "generate a correct hash for my email address" in {
      gravatar("inigo.surguy@gmail.com") must beMatching("12813defbeb5840dcd4bfe0a5d124704")
    }
    "be case-insensitive and ignore leading/trailing space " in {
      gravatar("  INIGO.surguy@Gmail.com ") must beMatching("12813defbeb5840dcd4bfe0a5d124704")
    }
    "use a local image if the email address is empty" in {
      gravatar("") must beMatching("/images/")
    }
    "use a local image if the email address is null" in {
      gravatar(null) must beMatching("/images/")
    }
  }

}