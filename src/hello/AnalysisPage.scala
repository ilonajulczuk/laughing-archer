package hello

import analysis.{CategoryClassifier, BookAnalyser}
import scalafx.scene.control.{ScrollPane, Label, TextArea}
import scalafx.scene.layout.VBox
import scalafx.geometry.Insets
import scalafx.scene.text.Font
import scalafx.stage.Stage

/**
 * AnalysisPage is shown to show analysis of
 * the books text.
 * It displays summary, most common words and category
 * to which this text was associated with mach percentage.
 * @param bookText - text of a book
 * @param shortenBookText - shorter version of the one above. Used for making summary
 * @param stage  - just an ordinary stage
 */
class AnalysisPage(bookText: String, shortenBookText: String, stage: Stage) extends ScrollPane {

  val analyser = new BookAnalyser
  val summaryText = analyser.makeSummary(shortenBookText)

  val summary = new TextArea {
    text = summaryText
    wrapText = true
    editable = false
  }

  val analysis = new VBox {
    padding = Insets(10)
    spacing = 10
    margin = Insets(10, 10, 10, 10)
    content = List(
      new Label("Results of analysis") {
        font = new Font("Verdana", 20)
      },
      new Label("Summary") {
        font = new Font("Verdana", 14)
      },
      summary,
      new Label("Most common words") {
        font = new Font("Verdana", 14)
      },
      new Label() {
        text = analyser.mostCommonWords(bookText).slice(0, 5).mkString(", ")
      },
      new Label("Associated categories") {
        font = new Font("Verdana", 14)
      },
      new Label() {
        var maxTextSize = bookText.size
        if (maxTextSize > 1000) {
          maxTextSize = 1000
        }
        val winningCategory = CategoryClassifier.matchCategory(bookText.slice(0, maxTextSize))
        text = "%s, with %1.0f percent match".format(winningCategory._1, winningCategory._2)
      }
    )
  }

  summary.prefHeight = 300
  summary.prefWidth.bind(analysis.prefWidthProperty)
  summary.prefColumnCount = 35
  summary.prefRowCount = 35

  margin = Insets(10, 10, 10, 10)
  prefWidth = 500
  prefHeight = 580
  content = analysis
}
