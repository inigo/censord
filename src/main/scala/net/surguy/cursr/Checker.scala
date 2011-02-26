package net.surguy.cursr

import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.json.JsonAST.JString
import net.liftweb.common.{Failure, Full, Empty}

/**
 * Respond to REST requests to the /api/ path.
 *
 * @author Inigo Surguy
 * @created 26/02/2011 13:09
 */
object Checker extends RestHelper {

  /** Determine which requests we will respond to - this is called via the dispatch table set up in Boot. */
  serve {
    case "api" :: "check" :: _ Get _ => for {text <- S.param("text") ?~ "Param 'text' is missing"} yield checkText(text)
  }

  def checkText(text: String) = <span class="response">{ "You supplied text of "+text }</span>

}
