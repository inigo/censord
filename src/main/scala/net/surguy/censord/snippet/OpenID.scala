package net.surguy.censord.snippet

import xml.NodeSeq
import net.liftweb.common.{Empty, Full}
import net.surguy.censord.LocalOpenIDVendor

/**
 * Displays the currently logged-in user.
 *
 * @author Inigo Surguy
 * @created 26/02/2011 20:39
 */
class OpenID {
  def renderForm(xhtml: NodeSeq) : NodeSeq = LocalOpenIDVendor.loginForm

  def user(xhtml: NodeSeq) : NodeSeq = LocalOpenIDVendor.currentUser match {
    case Full(username) => <span class="username"><img src="/images/openidico.png" width="25" height="25"
                                                       alt={ "Logged in as : "+username } title={ "Logged in as : "+username }/></span>
    case Empty => <span class="username"></span>
    case _ => <span class="username"></span>
  }
}