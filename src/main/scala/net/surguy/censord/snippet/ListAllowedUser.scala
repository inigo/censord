package net.surguy.censord.snippet

import net.liftweb.http.SHtml
import net.surguy.censord.model.AllowedUser
import net.liftweb.util.BindHelpers._
import net.liftweb.util.{TimeHelpers, CssBind}

import _root_.net.liftweb.http.SHtml._
import net.liftweb.http.js.JsCmds.SetHtml
import net.liftweb.common.Logger
import java.util.Date
import xml.{Group, Text}
import net.liftweb.http.js.jquery.JqJE.Jq
import net.liftweb.http.js.JE.ParentOf
import net.liftweb.http.js.jquery.JqJsCmds.FadeOut

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 27/02/2011 14:46
 */

class ListAllowedUser extends Logger {

  def list(): CssBind = ".line *" #> AllowedUser.findAll.map(
    w => ".username *" #> w.username
       & ".createdAt *" #> TimeHelpers.dateFormatter.format(w.createdAt.is)
       & ".allowed *" #> {
          val id = "itemAllowed"+w.primaryKeyField.is
          def txt() = if (w.allowed.is) Text("Yes") else Text("No")
          List(
            <span id={ id }>{ txt }</span>,
            ajaxButton("Toggle", {() =>
              w.allowed( !w.allowed.is ).save()
              SetHtml(id, txt() )
            }))
          }
       & ".allowed [class+]" #> { if (w.allowed.is) "good" else "bad" }
       & ".delete *" #> ajaxButton("Delete", {() =>
//          w.delete_!
          SetHtml( "line_"+w.id.is, Text(""))
        })
  )

}