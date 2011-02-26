package net.surguy.cursr.snippet

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 26/02/2011 11:39
 */

class Words {
  val words = List( Word("silly"), Word("foolish"), Word("wrong"))

  def list() = for (word <- words) yield <tr><td>{ word.text }</td><td><input type="checkbox"/></td></tr>
}

case class Word(text: String)