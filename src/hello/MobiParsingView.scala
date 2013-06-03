package hello

import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.{Orientation, Insets}
import scalafx.scene.text.Font
import scalafx.event.ActionEvent
import scalafx.stage.{Stage, FileChooser}
import mobireader.{MobiDescriptor, MobiContentParser, Mobi}
import javafx.scene.control.Dialogs
import scalafx.Includes._
import scalafx.scene.Node
import analysis.{CategoryClassifier, BookAnalyzer}

class MobiParsingView(model: AppModel, stage: Stage) extends SplitPane{

  val dialogStage = new Stage

  def createAnalysisPage(): Node = {
    val analyzer = new BookAnalyzer
    val summaryText = analyzer.makeSummary(model.shortenBookText)

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
        new Label("Assosiated categories") {
          font = new Font("Verdana", 14)
        },
        new Label() {
          var maxTextSize = model.bookText.size
          if (maxTextSize > 1000) {
            maxTextSize = 1000
          }
          val winningCategory = CategoryClassifier.matchCategory(model.bookText.slice(0, maxTextSize))
          text = winningCategory._1 + " with " + winningCategory._2 + "% match"
        }
      )
    }

    summary.prefHeight.bind(analysis.prefHeightProperty)
    summary.prefWidth.bind(analysis.prefWidthProperty)
    summary.prefColumnCount = 35
    summary.prefRowCount = 35

    new ScrollPane {
      margin = Insets(10, 10, 10, 10)
      prefWidth = 500
      prefHeight = 580
      content = analysis
    }
  }

  def showBookAnalysis() {
    val dialogStage = new Stage
    val page = createAnalysisPage()
    StageUtil.showPageInWindow(page, "Book Statistics", dialogStage)
  }

  val bookHtml = new TextArea {
    text = "No book html yet."
    wrapText = true
    editable = false
  }
  val bookText = new TextArea {
    text = "Content not available"
    wrapText = true
    editable = false
  }
  val left = new VBox {
    content = bookHtml
  }

  bookHtml.prefHeight.bind(left.prefHeightProperty)
  bookHtml.prefWidth.bind(left.prefWidthProperty)
  bookHtml.prefColumnCount = 35
  bookHtml.prefRowCount = 35
  bookText.prefRowCount = 35
  val right = new VBox {
    content = new HBox {
      content = bookText
    }
  }

  val header = new VBox {
    spacing = 10
    margin = Insets(10, 10, 10, 10)
    content = List(
      new Label {
        text = "Mobi book preview"
        font = new Font("Verdana", 20)
      },
      new HBox {
        vgrow = scalafx.scene.layout.Priority.ALWAYS
        hgrow = scalafx.scene.layout.Priority.ALWAYS
        spacing = 10
        val filePath = new Label {
          text = "No path"
        }

        def chooseFile() = {
          val fileChooser = new FileChooser()
          val result = fileChooser.showOpenDialog(dialogStage)
          try {
            result.getAbsolutePath
          }
          catch {
            case e: NullPointerException => ""
          }
        }

        val button = new Button("Choose file")  {
          onAction = loadFile _
        }

        def loadFile(e: ActionEvent) {
          (_: ActionEvent) =>
            try {
              val pathToFile = chooseFile()
              if (pathToFile != "" &&
                pathToFile.endsWith("mobi")
                || pathToFile.endsWith("bin")) {

                model.mobi = new Mobi(pathToFile)
                model.mobi.parse()
                val html = model.mobi.readAllRecords()
                val parser = new MobiContentParser(html)
                bookHtml.text = html
                val textToBeShown = parser.bodyWithParagraphs
                if (textToBeShown == "")
                  model.bookText = parser.bodyText
                else
                  model.bookText = textToBeShown
                filePath.text = model.filePath
                bookText.text = model.bookText
                metadataButton.visible = true
                analyzeButton.visible = true
              }
              else
                Dialogs.showWarningDialog(stage,
                  "File has to be in mobi format", "Reading failure")
            }
            catch {
              case e: NullPointerException => println("WTF, null pointer? Probably problem with file chooser")
            }
        }

        val metadataButton = new Button("Show metadata")
        metadataButton.onAction = showMetadata _

        metadataButton.visible = false

        val analyzeButton = new Button("Analyze content")
        analyzeButton.onAction_=({
          (_: ActionEvent) =>
            showBookAnalysis()
        })

        metadataButton.visible = false
        analyzeButton.visible = false
        content = List(
          filePath,
          button,
          metadataButton,
          analyzeButton
        )

      }  )
  }

  val body = new SplitPane {
    items.addAll(
      left,
      right
    )
  }

  orientation = Orientation.VERTICAL
  items.addAll(
    header,
    body
  )

  setDividerPosition(0, 0.1)

  def showMetadata(e: ActionEvent)  {
    if (model.mobi != null) {
      val descriptor = new MobiDescriptor(model.mobi)

      def addIndentation(paragraph: String) = {
        (for (sentence <- paragraph.split("\n"))
        yield "  " + sentence).mkString("\n") + "\n"
      }

      var description = "First header:\n" + addIndentation(descriptor.firstHeaderInfo)
      description += "\nPalmdoc header:\n" + addIndentation(descriptor.palmdocHeaderInfo)
      description += "\nMobi header:\n" + addIndentation(descriptor.mobiHeaderInfo)
      val dialogStage = new Stage
      Dialogs.showInformationDialog(dialogStage,
        description, "Mobi file contains multiple headers, " +
          "the most important have following data:", "Mobi metadata")
    }
  }


}
