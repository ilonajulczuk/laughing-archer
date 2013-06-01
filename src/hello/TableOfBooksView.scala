package hello

import scalafx.scene.control.{TableView, TableColumn}
import javafx.{util => jfxu}
import scalafx.Includes._
import javafx.beans.{value => jfxbv}
import javafx.scene.control.TableColumn.CellDataFeatures

import scalafx.scene.control._
import scalafx.beans.property.StringProperty
import scalafx.stage.Stage
import scalafx.scene.Node
import domain.Book


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
      new StringProperty(this, "Category", param.getValue.category)
  })

  def createManagementPage(book: Book, dialogStage: Stage): Node = {
    new BookManagementPage(book, dialogStage, model)
  }

  def showBookManagement(book: Book) {
    val dialogStage = new Stage()
    val page = createManagementPage(book, dialogStage)
    StageUtil.showPageInWindow(page, "Book Management", dialogStage)
  }

  val table = new TableView[Book](model.books) {
    delegate.getColumns.addAll(
      titleColumn.delegate,
      authorColumn.delegate,
      descriptionColumn.delegate,
      pathColumn.delegate,
      categoryColumn.delegate
    )
  }
  table.getSelectionModel.selectedItemProperty.onChange(
    (_, _, newValue) => {
      showBookManagement(newValue)
    }
  )
}
