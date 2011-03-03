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
    // @todo Provide statistics on API usage
    case "api" :: "check" :: _ Get _ => for {text <- S.param("text") ?~ "Param 'text' is missing"} yield checkText(text)
    // @todo Allow trusted users with an API key to download all current words via REST
//    case "api" :: "add" :: _ Post _ => for {text <- S.param("phrase") ?~ "Param 'phrase' is missing"} yield addPhrase(text)
  }

  def checkText(text: String) = {
    val result = new Checker(Phrase.findAll).checkText(text)
    result match {
      case Accepted() => <span class="check success"><span class="msg">Success</span></span>
      case Rejected(reasons) =>
        <span class="check failure">
          <span class="msg">Failure</span>
          <span class="reasons">{ reasons.mkString(" ") }</span>
        </span>
    }
  }

  def addPhrase(phrase: String) = {
    Phrase.createMultiplePhrases(phrase)
    <span class="add success">{ "New entry '"+phrase+"' created" }</span>
  }

}
