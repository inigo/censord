package net.surguy.cursr.model

import net.liftweb.mapper._

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
}

object Phrase extends Phrase with LongKeyedMetaMapper[Phrase] {


}
