package net.surguy.censord.snippet

import net.liftweb.util.CssBind
import net.liftweb.http.js.jquery.JqJsCmds.DisplayMessage
import net.surguy.censord.model.Phrase
import net.surguy.censord.Checker
import xml.Text
import net.liftweb.http.SHtml._
import net.liftweb.util.BindHelpers._
import net.liftweb.util.Helpers._

/**
 * Check text.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 19:22
 */
class CheckerSnippet {

  def check(): CssBind = "#checker *" #>
    ajaxTextarea("", text => {
      val isAcceptable = new Checker(Phrase.findAll).checkText(text)
      val results = if (isAcceptable) "Accepted" else "Rejected"
      DisplayMessage("messages", Text(results), 10 seconds, 1 second)
    })

}