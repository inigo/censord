package net.surguy.censord

import model.Phrase
import net.liftweb.http._
import net.liftweb.http.rest._
import java.util.Date

/**
 * Respond to REST requests to the /api/ path.
 *
 * @author Inigo Surguy
 * @created 26/02/2011 13:09
 */
object RestApi extends RestHelper {

  /** Determine which requests we will respond to - this is called via the dispatch table set up in Boot. */
  serve {
    case "api" :: "check" :: _ Get _ => for {text <- S.param("text") ?~ "Param 'text' is missing"} yield checkText(text)
    case "api" :: "add" :: _ Post _ => for {text <- S.param("phrase") ?~ "Param 'phrase' is missing"} yield addPhrase(text)
  }

  def checkText(text: String) = <span class="check success">{ "You supplied text of "+text }</span>

  def addPhrase(phrase: String) = {
    Phrase.createMultiplePhrases(phrase)
    <span class="add success">{ "New entry '"+phrase+"' created" }</span>
  }

}
