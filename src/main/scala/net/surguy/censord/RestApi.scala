package net.surguy.censord

import model.Phrase
import net.liftweb.http._
import net.liftweb.http.rest._
import java.util.Date
import net.surguy.censord.model.AllowedUser

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
    case "api" :: "terms" :: _ Post _ =>
      (for {text <- S.param("terms") ?~ "Param 'terms' is missing";
            key <- S.param("apiKey") ?~ "Param 'apiKey' is missing" if AllowedUser.isValid(key)}
              yield addPhrase(text)) ?~ "apiKey is invalid"
    case "api" :: "terms" :: _ Get _ =>
      (for {key <- S.param("apiKey") ?~ "Param 'apiKey' is missing" if AllowedUser.isValid(key)}
        yield getTerms()) ?~ "apiKey is invalid"
    case "api" :: "users" :: _ Get _ =>
      (for {key <- S.param("apiKey") ?~ "Param 'apiKey' is missing" if AllowedUser.isValid(key)}
        yield getUsers()) ?~ "apiKey is invalid"
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

  def getTerms() = <terms> {for (phrase <- Phrase.findAll) yield phrase.toXml } </terms>
  def getUsers() = <users> {for (user <- AllowedUser.findAll) yield user.toXml } </users>

}
