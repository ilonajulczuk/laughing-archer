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
}