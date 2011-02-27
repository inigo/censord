package net.surguy.censord

import model.AllowedUser
import org.openid4java.discovery.Identifier
import org.openid4java.consumer.VerificationResult
import net.liftweb.openid.SimpleOpenIDVendor
import net.liftweb.openid.OpenIDUser
import net.liftweb.common.{Full, Box}
import net.liftweb.http.S

/**
 * Manages OpenID logins - creating a new entry in the database on login for a user who hasn't been
 * seen before.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 16:58
 */

object LocalOpenIDVendor extends SimpleOpenIDVendor {

  // @todo Retrieve email address for OpenID users
  // http://scala-tools.org/mvnsites/liftweb-2.0/framework/scaladocs/net/liftweb/openid/Extensions.scala.html
  override def postLogin(id: Box[Identifier], res: VerificationResult) = {
    id match {
      case Full(id) => AllowedUser.createIfNew(id.getIdentifier)
    }
    OpenIDUser(id)
  }
}