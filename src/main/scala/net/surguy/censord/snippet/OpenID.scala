package net.surguy.censord.snippet

import xml.NodeSeq
import net.liftweb.openid.SimpleOpenIDVendor

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 26/02/2011 20:39
 */
class OpenID {
  def renderForm(xhtml: NodeSeq) : NodeSeq = SimpleOpenIDVendor.loginForm
}