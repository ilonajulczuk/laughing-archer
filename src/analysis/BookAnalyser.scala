package analysis

/**
 * Book analyser performs analysis of the content of the book.
 * It uses SummaryTool which given text prepares summary.
 */

class BookAnalyser {
  val summaryTool = new SummaryTool

  def makeSummary(text: String) = {
    val sentencesMap = summaryTool.getSentecesRanks(text)
    summaryTool.getSummary(text, sentencesMap)
  }

  def mostCommonWords(text: String) = {
    val words = CategoryClassifier.getWordsFromRawText(text)
    (for (wordTuple <- CategoryClassifier.rankWords(words).slice(0, 20)
         if wordTuple._1.size > 1 && ! wordTuple._1.contains("=") ) yield wordTuple._1).toList
  }
}