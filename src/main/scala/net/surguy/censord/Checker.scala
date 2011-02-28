package net.surguy.censord

import model.Phrase
import WordUtils._

/**
 * Check text and determine whether it contains words in the database.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 18:57
 */
class Checker(phrases: List[Phrase]) {

  val simpleTerms = phrases.filter( ! _.stemming.is ).map( _.word.is ).map( _.toLowerCase )
  val stemmedTerms = phrases.filter( _.stemming.is ).map( _.word.is ).map( _.toLowerCase ).map( WordUtils.stem )

  // @todo Report why a rejection occurred
  def checkText(text: String): Boolean = {
    val words = text.toLowerCase.words.toList
    words.intersect( simpleTerms ).isEmpty && words.map( WordUtils.stem ).intersect( stemmedTerms ).isEmpty
  }

}