package net.surguy.censord.snippet

import net.liftweb.http.{S, LiftScreen, SHtml}
import net.surguy.censord.model.Phrase
import java.util.Date

/**
 * Form to create more words.
 *
 * @author Inigo Surguy
 * @created 26/02/2011 18:11
 */

object WordScreen extends LiftScreen {
  // @todo There must be a more concise way of specifying that we're using a textarea?
  val words = new Field {
    type ValueType = String
    override def name = "Add new words (comma-separated)"
    override implicit def manifest = buildIt[String]
    override def default = ""
    override def toForm = SHtml.textarea(is, set _)
    override def validations = List(valMinLen(1, "You must supply a word"), valMaxLen(50, "Too many words!"))
  }

  def finish() {
    Phrase.createMultiplePhrases(words.is)
    S.notice("New words created")
  }
}