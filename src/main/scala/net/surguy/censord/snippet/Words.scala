package net.surguy.censord.snippet

import net.liftweb.util.BindHelpers._
import net.liftweb.util.CssBind
import net.surguy.censord.model.Phrase

/**
 * Manage the list of words that should be checked for.
 *
 * @author Inigo Surguy
 * @created 26/02/2011 11:39
 */

class Words {
  def list(): CssBind = ".line *" #> Phrase.findAll.map( ".word" #> _.word )
}