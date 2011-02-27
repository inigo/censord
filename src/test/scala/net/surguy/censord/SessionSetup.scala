package net.surguy.censord

import net.liftweb.common.Empty
import net.liftweb.http.{S, LiftSession}
import net.liftweb.util.StringHelpers._
import org.specs.Specification


/**
 * Mix-in for tests that sets up a session for them.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 10:09
 */

trait SessionSetup extends Specification {
  val session = new LiftSession("", randomString(20), Empty)

  doBeforeSpec { setupSession() }

  def setupSession() = {
    S.initIfUninitted(session) { }
  }

}