package hello

import domain.Book
import scalafx.scene.control.{Button, Label, ScrollPane}
import scalafx.scene.layout.{TilePane, VBox}
import scalafx.geometry.Insets
import scalafx.stage.Stage
import scalafx.scene.text.Font
import scala.collection.JavaConversions._
import java.text.SimpleDateFormat
import scalafx.event.ActionEvent
import scalafx.Includes._

/**
 * Created with IntelliJ IDEA.
 * User: att
 * Date: 6/10/13
 * Time: 11:02 AM
 */

class BookInfoPage(book: Book, model: AppModel, stage: Stage) extends ScrollPane {

  val mainBox = new VBox {
    spacing = 10
    padding = Insets(20, 10, 10, 20)
    val title = new Label {
      text = book.title
      font = new Font("Verdana", 20)
    }
    val description = new Label {
      if(book.description.nonEmpty)
        text = book.description
      else
        text = "No description yet"
    }

    val category = new Label {
      text = book.category.category
    }

    val path = new Label {
      text = "Located in %s".format(book.getPathToContent)
    }

    val tags = new Label {
      if (book.tags.isEmpty) {
        text = "There aren't any tags associated with this book"
      }
      else {
        text = (for( tag <- book.tags) yield tag.tag)mkString(", ")
      }
    }

    val authorName = new Label {
      text = "Written by %s".format(book.getAuthor.name)
    }

    val addedToDB = new Label {
      val dateFormat = new SimpleDateFormat("dd/MM/yyyy")
      text = "Added to database on: %s ".format(dateFormat.format(book.getTimestamp))
    }

    val decisionButtons = new TilePane {
      padding = Insets(20, 10, 20, 0)
      hgap = 8
      content = List(
        new Button("Show book text") {
          onAction = {e: ActionEvent => {
            val bookText = model.fetchBookText(book)
            val page = new TextOfBookPage(bookText)
            StageUtil.showPageInWindow(page,"Text of %s".format(book.title), stage)
          }}
        },
        new Button("Show content analysis") {
          onAction = { e: ActionEvent => {
            val bookText = model.fetchBookText(book)
            val page = new AnalysisPage(bookText, model.shortenBookText(bookText), stage)
            StageUtil.showPageInWindow(page,"Content analysis of %s".format(book.title), stage)
          }}
        }
      )
    }

    content = List(
      title,
      description,
      category,
      addedToDB,
      authorName,
      tags,
      decisionButtons
    )
    prefHeight = 290
    prefWidth = 390
  }
  prefHeight = 300
  prefWidth = 400
  content = mainBox
}
