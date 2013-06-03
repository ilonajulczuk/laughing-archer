package hello

import scalafx.scene.layout.VBox

import scalafx.stage.Stage
import scalafx.scene.control._
import scalafx.scene.text.Font
import domain.{PrioritizedBook, BookOrganizer}
import javafx.scene.control.TableColumn.CellDataFeatures
import scalafx.beans.property.StringProperty


import javafx.{util => jfxu}
import scalafx.Includes._
import javafx.beans.{value => jfxbv}
import scalafx.collections.ObservableBuffer

class ReadingPlanView(model: AppModel, stage: Stage) extends ScrollPane {
  val organizer = new BookOrganizer

  def createPriorityBookTable(books: ObservableBuffer[PrioritizedBook]) : TableView[PrioritizedBook] = {
    val titleColumn = new TableColumn[PrioritizedBook, String]("Title") {
      prefWidth = 180
    }

    titleColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Title", param.getValue.title)
    })

    val priorityColumn = new TableColumn[PrioritizedBook, String]("Title") {
      prefWidth = 180
    }
    priorityColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Title", param.getValue.title)
    })

    val deadlineColumn = new TableColumn[PrioritizedBook, String]("Title") {
      prefWidth = 180
    }
    deadlineColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Title", param.getValue.title)
    })

    val progressColumn = new TableColumn[PrioritizedBook, String]("Title") {
      prefWidth = 180
    }
    progressColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Title", param.getValue.title)
    })

    val table = new TableView[PrioritizedBook]() {
      delegate.getColumns.addAll(
        titleColumn.delegate,
        priorityColumn.delegate,
        deadlineColumn.delegate,
        progressColumn.delegate
      )
    }
    table.getSelectionModel.selectedItemProperty.onChange(
      (_, _, newValue) => {
        println(newValue)
      }
    )
    table
  }

  val mainBox = new VBox {
    val nextToRead: ObservableBuffer[PrioritizedBook] =
      ObservableBuffer[PrioritizedBook](for (book <-
                                             organizer.getFirst(5)) yield book)

    content = List(
      new Label {
        text = "Next to be read:"
        font = new Font("Verdana", 20)
      },
      createPriorityBookTable(nextToRead)
    )
  }

  content = mainBox
}
