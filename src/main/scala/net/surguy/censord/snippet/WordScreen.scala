package net.surguy.censord.snippet

import net.liftweb.http.{S, LiftScreen}
import net.surguy.censord.model.Phrase
import java.util.Date

/**
 * Form to create more words.
 *
 * @author Inigo Surguy
 * @created 26/02/2011 18:11
 */

object WordScreen extends LiftScreen {
  val words = field(S ? "Comma-separated words", "",
                       trim,
                       valMinLen(1, "You must supply a word"),
                       valMaxLen(50, "Too many words!"))

  def finish() {
    // @todo Dupes code in Checker
    for (word <- words.is.split(","); val trimmed = word.trim if trimmed.length > 0) {
      val newPhrase = Phrase.create
      newPhrase.word(trimmed).stemming( trimmed.endsWith("*") ).createdAt( new Date() )
      newPhrase.save()
    }
    S.notice("New words created")
  }
}