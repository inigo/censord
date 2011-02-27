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

  "ensuring there is a default user" should {
    "create a user if there isn't already one" in {
      deleteAll()
      AllowedUser.ensureDefaultUser()
      AllowedUser.findAll.size must beEqualTo(1)
    }
    "do nothing when there already are users" in {
      deleteAll()
      AllowedUser.create.username("test").save()
      AllowedUser.findAll.size must beEqualTo(1)
      AllowedUser.ensureDefaultUser()
      AllowedUser.findAll.size must beEqualTo(1)
    }
  }

  private def deleteAll() = {
    AllowedUser.findAll.foreach( _.delete_! )
    AllowedUser.findAll.size must beEqualTo(0)
  }

}