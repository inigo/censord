package net.surguy.censord.snippet

import net.liftweb.http.SHtml
import net.surguy.censord.model.AllowedUser
import net.liftweb.util.BindHelpers._
import net.liftweb.util.{TimeHelpers, CssBind}

import _root_.net.liftweb.http.SHtml._
import net.liftweb.common.Logger
import java.util.Date
import xml.{Group, Text}
import net.liftweb.http.js.jquery.JqJE.Jq
import net.liftweb.http.js.JE.ParentOf
import net.liftweb.http.js.jquery.JqJsCmds.FadeOut
import net.liftweb.http.js.JE
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.{Run, SetHtml}
import net.surguy.censord.LocalOpenIDVendor

/**
 * Display the current list of users, toggle whether they're allowed to use the system or not,
 * and allow deletion of users.
 * <p>
 * The user list is those users who have ever successfully logged into the system via OpenID.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 14:46
 */
class AllowedUserSnippet extends Logger {

  def list(): CssBind = ".line *" #> AllowedUser.findAll.map(
    w => ".username *" #> w.username
       & ".createdAt *" #> TimeHelpers.dateFormatter.format(w.createdAt.is)
       & ".allowed *" #> { LocalOpenIDVendor.currentUser.get match {
          case u if u.toString == w.username.is.toString => Text("")
          case _ =>
            val id = "itemAllowed"+w.primaryKeyField.is
            def txt() = if (w.allowed.is) Text("Yes") else Text("No")
            List(
              <span id={ id }>{ txt }</span>,
              ajaxButton("Toggle", {() =>
                w.allowed( !w.allowed.is ).save()
                SetHtml(id, txt() )
              }))
            }
          }
       & ".allowed [class+]" #> { if (w.allowed.is) "good" else "bad" }
       & ".delete *" #> { LocalOpenIDVendor.currentUser.get match {
          case u if u.toString == w.username.is.toString => Text("")
          case _ =>
            ajaxButton("Delete", {() =>
            w.delete_!
            Run("window.location.reload()") // Runs arbitrary JavaScript
          })
        } }
  )

}