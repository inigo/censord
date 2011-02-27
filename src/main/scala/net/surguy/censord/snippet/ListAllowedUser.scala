package net.surguy.censord.snippet

import net.liftweb.http.SHtml
import net.surguy.censord.model.AllowedUser
import net.liftweb.util.BindHelpers._
import net.liftweb.util.{TimeHelpers, CssBind}


/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 27/02/2011 14:46
 */

class ListAllowedUser {

  def list(): CssBind = ".line *" #> AllowedUser.findAll.map(
    w => ".username *" #> w.username
       & ".createdAt *" #> TimeHelpers.dateFormatter.format(w.createdAt.is)
       & ".allowed *" #> { if (w.allowed.is) "Yes" else "No" }
       & ".allowed [class+]" #> { if (w.allowed.is) "good" else "bad" }
       & ".delete [href]" #> ("delete/" + w.primaryKeyField.is)
  )

}