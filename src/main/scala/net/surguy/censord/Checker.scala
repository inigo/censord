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

  def checkText(text: String): ValidationResult = {
    val words = text.toLowerCase.words.toList
    val failedWords = words.intersect( simpleTerms ) ::: words.map( WordUtils.stem ).intersect( stemmedTerms )
    if (failedWords.isEmpty) Accepted() else Rejected(failedWords)
  }

}

sealed abstract class ValidationResult(val isAccepted: Boolean)
case class Accepted() extends ValidationResult(true)
case class Rejected(words: List[String]) extends ValidationResult(false)
