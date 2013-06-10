package hello

import analysis.{CategoryClassifier, BookAnalyzer}
import scalafx.scene.control.{ScrollPane, Label, TextArea}
import scalafx.scene.layout.VBox
import scalafx.geometry.Insets
import scalafx.scene.text.Font
import scalafx.stage.Stage

/**
 * Created with IntelliJ IDEA.
 * User: att
 * Date: 6/10/13
 * Time: 10:35 AM
 */
class AnalysisPage(bookText: String, shortenBookText: String, stage: Stage) extends ScrollPane {

  val analyzer = new BookAnalyzer
  val summaryText = analyzer.makeSummary(shortenBookText)

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
      new Label("Associated categories") {
        font = new Font("Verdana", 14)
      },
      new Label() {
        var maxTextSize = bookText.size
        if (maxTextSize > 1000) {
          maxTextSize = 1000
        }
        val winningCategory = CategoryClassifier.matchCategory(bookText.slice(0, maxTextSize))
        text = "%s, with %1.0f percent match?".format(winningCategory._1, winningCategory._2)
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
