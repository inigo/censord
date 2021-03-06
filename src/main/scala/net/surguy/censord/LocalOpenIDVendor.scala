package net.surguy.censord

import model.AllowedUser
import org.openid4java.consumer.VerificationResult
import net.liftweb.http.S
import org.openid4java.message.AuthRequest
import org.openid4java.discovery.{DiscoveryInformation, Identifier}
import net.liftweb.openid._
import net.liftweb.common.{Logger, Empty, Full, Box}

/**
 * Manages OpenID logins - creating a new entry in the database on login for a user who hasn't been
 * seen before.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 16:58
 */

object LocalOpenIDVendor extends SimpleOpenIDVendor with Logger {

  override def createAConsumer = new AnyRef with OpenIDConsumer[UserType] {

    def addParams(di : DiscoveryInformation, authReq: AuthRequest): Unit = {
      import WellKnownAttributes._
      WellKnownEndpoints.findEndpoint(di) map {ep =>
        ep.makeAttributeExtension(List(Email, FullName, FirstName, LastName)) foreach { ex => authReq.addExtension(ex) }
      }
    }

    beforeAuth = Box(addParams _)
  }

  override def postLogin(id: Box[Identifier], res: VerificationResult) = {
    id match {
      case Full(id) =>
        val user = AllowedUser.createIfNew(id.getIdentifier)

        import WellKnownAttributes._
        val attrs = WellKnownAttributes.attributeValues(res.getAuthResponse)
        attrs.get(Email) map {e => user.email(trace("Extracted email",e))}

        val firstName: Option[String] = attrs.get(FirstName)
        val lastName: Option[String] = attrs.get(LastName)
        // This is clumsy - should be dealing with options better
        val combinedName : Option[String] = Some((firstName getOrElse "") +
          (if (firstName.isDefined && lastName.isDefined) " " else "" ) +
          (lastName getOrElse ""))

        attrs.get(FullName) orElse combinedName map {l => user.realName( trace("Extracted name",l)) }
        user.save()
      case _ =>
    }
    OpenIDUser(id)
  }
}