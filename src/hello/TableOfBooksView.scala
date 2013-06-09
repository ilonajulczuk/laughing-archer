package hello

import javafx.{util => jfxu}
import scalafx.Includes._
import javafx.beans.{value => jfxbv}
import javafx.scene.control.TableColumn.CellDataFeatures

import scalafx.scene.control._
import scalafx.beans.property.StringProperty
import scalafx.stage.Stage
import scalafx.scene.Node
import domain.Book
import scalafx.event.ActionEvent
import javafx.scene.control.Dialogs


class TableOfBooksView(model: AppModel, stage: Stage) {
  val titleColumn = new TableColumn[Book, String]("Title") {
    prefWidth = 180
  }
  titleColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
    def call(param: CellDataFeatures[Book, String]) = new StringProperty(this, "Title", param.getValue.getTitle)
  })

  val authorColumn = new TableColumn[Book, String]("Author name") {
    prefWidth = 180
  }
  authorColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
    def call(param: CellDataFeatures[Book, String]) =
      new StringProperty(this, "AuthorName", param.getValue.getAuthor.getName)
  })

  val descriptionColumn = new TableColumn[Book, String]("Description") {
    prefWidth = 180
  }
  descriptionColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
    def call(param: CellDataFeatures[Book, String]) =
      new StringProperty(this, "Description", param.getValue.description)
  })

  val pathColumn = new TableColumn[Book, String]("Path") {
    prefWidth = 180
  }
  pathColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
    def call(param: CellDataFeatures[Book, String]) =
      new StringProperty(this, "Path", param.getValue.getPathToContent)
  })

  val categoryColumn = new TableColumn[Book, String]("Category") {
    prefWidth = 180
  }
  categoryColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
    def call(param: CellDataFeatures[Book, String]) =
      new StringProperty(this, "Category", param.getValue.category.category)
  })

  def createChangePage(book: Book, dialogStage: Stage): Node = {
    new BookChangePage(book, dialogStage, model)
  }

  def showBookChangePage(book: Book) {
    val dialogStage = new Stage()
    val page = createChangePage(book, dialogStage)
    StageUtil.showPageInWindow(page, "Change your book", dialogStage)
  }



  val table = new TableView[Book](model.books) {
    delegate.getColumns.addAll(
      titleColumn.delegate,
      authorColumn.delegate,
      descriptionColumn.delegate,
      pathColumn.delegate,
      categoryColumn.delegate
    )

    val bookOptions = new ContextMenu {
      val change = new MenuItem("Change") {
        onAction = {
          e: ActionEvent =>  {
            showBookChangePage(selectionModel.value.selectedItemProperty().value)
          }
        }
      }

      val remove = new MenuItem("Remove") {
        onAction = {
          e: ActionEvent =>  {

            val dialogResponse = Dialogs.showConfirmDialog(new Stage,
              "Remove a book",
              "Are you sure, that you want to remove this book?", "Remove confirmation")
            if (dialogResponse == Dialogs.DialogResponse.YES) {
              val book = selectionModel.value.selectedItemProperty().value
              model.db.removeBook(book)
              model.books.remove(book)
              model.updateNamesOfAuthors()
              Dialogs.showInformationDialog(new Stage, "Book was removed", "Removal complete", "Book removal info")
            }

          }
        }
      }

      val moreInfo =  new MenuItem("More info") {
        onAction = {
          e: ActionEvent =>  {
            println("More info isn't available yet")
          }
        }
      }

      items ++= List(
        remove,
        change,
        moreInfo
      )
    }
    contextMenu = bookOptions

  }
}
