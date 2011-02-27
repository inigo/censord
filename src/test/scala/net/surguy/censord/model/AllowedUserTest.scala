package net.surguy.censord.model

import org.specs.SpecificationWithJUnit
import org.specs.specification.Examples
import net.liftweb.common.Empty
import net.liftweb.http.{LiftSession, S}
import net.liftweb.util.StringHelpers._
import bootstrap.liftweb.Boot
import net.liftweb.util.Props
import net.liftweb.mapper.{DefaultConnectionIdentifier, StandardDBVendor, DB}

/**
 * Test managing allowed users.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 09:23
 */

class AllowedUserTest extends SpecificationWithJUnit {
  val session = new LiftSession("", randomString(20), Empty)

  doBeforeSpec {
    setupDatabase()
  }

  def setupSession() = {
    S.initIfUninitted(session) { }
  }

  def setupDatabase() = {
    println("Setting up database")
    val vendor = new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
       Props.get("db.url") openOr
       "jdbc:h2:lift_proto_test.db;AUTO_SERVER=TRUE",
       Props.get("db.user"), Props.get("db.password"))
    DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    new Boot().createDatabaseTables()
  }

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