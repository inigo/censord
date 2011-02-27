package net.surguy.censord

import model.Phrase

/**
 * Check text and determine whether it contains words in the database.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 18:57
 */
class Checker(phrases: List[Phrase]) {

  val simpleTerms = phrases.filter( ! _.stemming.is ).map( _.word.is )
  val stemmedTerms = phrases.filter( _.stemming.is ).map( _.word.is )

  def checkText(text: String): Boolean = {
    simpleTerms.find( text.contains(_) ).isEmpty
  }

}