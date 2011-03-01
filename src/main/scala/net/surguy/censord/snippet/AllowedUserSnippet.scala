package net.surguy.censord.snippet

import net.liftweb.http.SHtml
import net.surguy.censord.model.AllowedUser
import net.liftweb.util.BindHelpers._
import net.liftweb.util.{TimeHelpers, CssBind}

import _root_.net.liftweb.http.SHtml._
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
import net.liftweb.common.{Box, Logger}
import java.security.MessageDigest

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

  private val iconSize = "42"

  def list(): CssBind = ".line *" #> AllowedUser.findAll.map(
    w =>
         ".icon *" #> <img src={ gravatarUrl(w.email.is) } alt={ w.username } title={ w.username } width={iconSize} height={iconSize} />
       & ".realName *" #> w.realName
       & ".email *" #> w.email
       & ".createdAt *" #> TimeHelpers.dateFormatter.format(w.createdAt.is)
       & ".allowed *" #> { LocalOpenIDVendor.currentUser.get match {
          case u if u.toString == w.username.is.toString => Text("")
          case _ =>
            val id = "itemAllowed"+w.primaryKeyField.is
            def txt() = if (w.allowed.is) Text("Allowed") else Text("Banned")
            List(
              <span id={ id }>{ txt }</span>,
              ajaxButton(if (w.allowed.is) "Toggle" else "Toggle", {() =>
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

  def gravatarUrl(email: String) = {
    email match {
      case null => "/images/unknownUser.png"
      case s if s.trim=="" => "/images/unknownUser.png"
      case _ =>
        val digest = MessageDigest.getInstance("MD5").digest(email.toLowerCase().trim.getBytes("UTF-8"))
        val hexDigest = BigInt(1,digest).toString(16)
        "http://www.gravatar.com/avatar/"+hexDigest+"?d=monsterid&s="+iconSize
    }
  }

}