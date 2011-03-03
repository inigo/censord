package net.surguy.censord.snippet

import net.liftweb.util.BindHelpers._
import net.liftweb.util.CssBind
import net.surguy.censord.model.Phrase
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds.{Run, SetHtml}
import _root_.net.liftweb.http.SHtml._
import xml.{Group, Text}

/**
 * Manage the list of words that should be checked for.
 *
 * @author Inigo Surguy
 * @created 26/02/2011 11:39
 */
class PhrasesSnippet {

  def list(): CssBind = ".line *" #> Phrase.findAll.map(
    w => ".word *" #>
          Group(<span class="rude">{ w.word }</span>
                <span class="filtered">{ disemvowel(w.word) }</span>)
       & ".stemming *" #> {
          val id = "itemStemming"+w.primaryKeyField.is
          def txt() = if (w.stemming.is) Text("Yes") else Text("No")
          List(
            <span id={ id }>{ txt }</span>,
            ajaxButton("Toggle", {() =>
              w.stemming( !w.stemming.is ).save()
              SetHtml(id, txt() )
            }))
          }
       & ".stemming [class+]" #> { if (w.stemming.is) "good" else "bad" }
       & ".delete *" #> ajaxButton("Delete", {() =>
          w.delete_!
          Run("window.location.reload()") // Runs arbitrary JavaScript
        })
  )

  def disemvowel(text: String) = text.replaceAll("[aeiouAEIOU]","*")

}
