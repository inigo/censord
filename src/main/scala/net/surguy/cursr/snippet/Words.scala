package net.surguy.cursr.snippet

import xml.NodeSeq
import net.liftweb.util.BindHelpers._

/**
 * Manage the list of words that should be checked for
 *
 * @author Inigo Surguy
 * @created 26/02/2011 11:39
 */

class Words {
  val words = List( Word("silly"), Word("foolish"), Word("bad"))

  def list(xhtml: NodeSeq): NodeSeq =
    words.flatMap(word => bind("f", xhtml, "text" --> word.text))

}

case class Word(text: String)