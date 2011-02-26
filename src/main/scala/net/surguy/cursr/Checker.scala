package net.surguy.cursr

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
object Checker extends RestHelper {

  /** Determine which requests we will respond to - this is called via the dispatch table set up in Boot. */
  serve {
    case "api" :: "check" :: _ Get _ => for {text <- S.param("text") ?~ "Param 'text' is missing"} yield checkText(text)
    case "api" :: "add" :: _ Get _ => for {text <- S.param("phrase") ?~ "Param 'phrase' is missing"} yield addPhrase(text)
  }

  def checkText(text: String) = <span class="check success">{ "You supplied text of "+text }</span>

  // @todo Move into another class
  def addPhrase(phrase: String) = {
    val newPhrase: Phrase = Phrase.create
    // @todo Should check for word uniqueness
    newPhrase.word(phrase).stemming( phrase.endsWith("*") ).createdAt( new Date() )
    newPhrase.save()
    <span class="add success">{ "New entry '"+phrase+"' created" }</span>
  }

}
