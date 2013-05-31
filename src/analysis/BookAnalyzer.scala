package analysis

class BookAnalyzer {
  val summaryTool = new SummaryTool
  def makeSummary(text: String) = {
    val sentencesMap = summaryTool.getSentecesRanks(text)
    summaryTool.getSummary(text, sentencesMap)
  }
}