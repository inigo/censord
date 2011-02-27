package net.surguy.censord.model

import org.specs.SpecificationWithJUnit
import net.surguy.censord.DatabaseSetup

/**
 * Test managing allowed users.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 09:23
 */

class AllowedUserTest extends SpecificationWithJUnit with DatabaseSetup {

  "creating users" should {
    "set allowed to be true for the first user" in {
      deleteAll()
      AllowedUser.createIfNew("test").allowed.is must beTrue
      AllowedUser.findAll.size must beEqualTo(1)
      AllowedUser.createIfNew("test2").allowed.is must beFalse
    }
    "set allowed to be false for subsequent users" in {
      deleteAll()
      AllowedUser.createIfNew("test")
      AllowedUser.createIfNew("test2").allowed.is must beFalse
    }
  }

  private def deleteAll() = {
    AllowedUser.findAll.foreach( _.delete_! )
    AllowedUser.findAll.size must beEqualTo(0)
  }

}