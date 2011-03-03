package net.surguy.censord.model

import net.liftweb.mapper._
import net.liftweb.common.Full
import net.liftweb.sitemap.{Loc, Menu}
import java.util.Date

/**
 * A word or phrase stored in the backing store.
 *
 * @author Inigo Surguy
 * @created 26/02/2011 15:30
 */

class Phrase extends LongKeyedMapper[Phrase] {
  def getSingleton = Phrase

  def primaryKeyField = id
  object id extends MappedLongIndex(this)
  object word extends MappedString(this, 50)
  object stemming extends MappedBoolean(this)
//  object severity extends MappedEnum(this)

  object createdAt extends MappedDateTime(this)
//  object createdBy extends MappedForeignKey(this)

  override def toXml = <term stemming={ ""+stemming }>{word}</term>
}

object Phrase extends Phrase with LongKeyedMetaMapper[Phrase] with CRUDify[Long, Phrase] {

  override def showAllMenuLoc = Full(Menu(Loc("PhraseList", List("phrase", "list"), "Terms")))

  def createMultiplePhrases(text: String) = {
    for (word <- text.split(","); val trimmed = word.trim if trimmed.length > 0) {
      val newPhrase = Phrase.create
      val usingStemming = trimmed.endsWith("*")
      val withoutStar = if (usingStemming) trimmed.substring(0, trimmed.size-1) else trimmed

      find(By(Phrase.word, withoutStar)).openOr{
        newPhrase.word( withoutStar ).stemming( usingStemming ).createdAt( new Date() ).save()
      }
    }
  }

}
