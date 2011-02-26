package net.surguy.cursr.snippet

import xml.NodeSeq
import net.liftweb.util.BindHelpers._
import net.liftweb.util.CssBind

/**
 * Manage the list of words that should be checked for.
 *
 * @author Inigo Surguy
 * @created 26/02/2011 11:39
 */

class Words {
  val words = List( Word("naughty"), Word("foolish"), Word("bad"))

  def list(): CssBind = ".line *" #> words.map( ".word" #> _.text )

}

case class Word(text: String)