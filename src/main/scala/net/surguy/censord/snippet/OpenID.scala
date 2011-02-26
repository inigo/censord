package net.surguy.censord.snippet

import xml.NodeSeq
import net.liftweb.openid.SimpleOpenIDVendor
import net.liftweb.common.{Empty, Full}

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 26/02/2011 20:39
 */
class OpenID {
  def renderForm(xhtml: NodeSeq) : NodeSeq = SimpleOpenIDVendor.loginForm

  // @todo Make pretty http://wstrange.wordpress.com/2009/11/23/adding-openid-support-to-a-scala-lift-application/

  def user(xhtml: NodeSeq) : NodeSeq = SimpleOpenIDVendor.currentUser match {
    case Full(username) => <span class="username">{ username }</span>
    case Empty => <span class="username">Not logged in</span>
  }
}