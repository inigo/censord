package net.surguy.censord

import java.text.BreakIterator
import org.tartarus.snowball.ext.EnglishStemmer


/**
 * Utilities for manipulating words within text.
 *
 * @author Inigo Surguy
 * @created 28/02/2011 20:22
 */

object WordUtils {

  private object Stemmer {
    val snowball = new EnglishStemmer()
    def stem(word: String) = {
      snowball.setCurrent(word)
      snowball.stem()
      snowball.getCurrent
    }
  }

  def stem(s: String) = Stemmer.stem(s)

  /** Extends string with methods to get sentences, words, and lines, using the default locale. */
  implicit def StringWithBreaks(s: String) = new {
    def sentences(): Iterator[String] = new BreakIt(s, BreakIterator.getSentenceInstance)
    def words(): Iterator[String] = new BreakIt(s, BreakIterator.getWordInstance).filter(word => word(0).isLetterOrDigit)
    def lines(): Iterator[String] = new BreakIt(s, BreakIterator.getLineInstance)
  }

  /**
   * Convert a Java BreakIterator into a Scala iterator (note that the BreakIterator is not
   * actually a Java Iterator).
   * <p>
   * By Rex Kerr, taken from http://comments.gmane.org/gmane.comp.lang.scala.user/23536
   */
  private class BreakIt(target: String, bi: BreakIterator) extends Iterator[String] {
      bi.setText(target)
      private var start = bi.first
      private var end = bi.next
      def hasNext = end != BreakIterator.DONE
      def next = {
        val result = target.substring(start,end)
        start = end
        end = bi.next
        result
      }
  // Alternative approach from Daniel Sobral in the same thread:
  //    Iterator.iterate(bi.first)(_ => bi.next).takeWhile (_ != BreakIterator.DONE).sliding(2)
  //            .map { case List(start, end) => text.substring(start, end) }
  }


}