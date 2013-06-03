package hello

import scalafx.scene.layout.{TilePane, HBox, VBox}

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
import scalafx.geometry.{Pos, Insets}
import scalafx.scene.Node


class ReadingPlanView(model: AppModel, stage: Stage) extends ScrollPane {
  val organizer = new BookOrganizer

  def createPriorityBookTable(books: ObservableBuffer[PrioritizedBook], customPlaceholder: Node = null) : TableView[PrioritizedBook] = {
    val titleColumn = new TableColumn[PrioritizedBook, String]("Title") {
      prefWidth = 180
    }

    titleColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Title", param.getValue.title)
    })

    val priorityColumn = new TableColumn[PrioritizedBook, String]("Priority") {
      prefWidth = 180
    }
    priorityColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Priority", param.getValue.title)
    })

    val deadlineColumn = new TableColumn[PrioritizedBook, String]("Deadline") {
      prefWidth = 180
    }
    deadlineColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Deadline", param.getValue.title)
    })

    val progressColumn = new TableColumn[PrioritizedBook, String]("Progress") {
      prefWidth = 180
    }
    progressColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Progress", param.getValue.title)
    })

    val table = new TableView[PrioritizedBook]() {
      prefHeight = 200
      prefWidth = 700

      if (customPlaceholder != null)
        placeholder = customPlaceholder
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
    padding = Insets(20, 10, 10, 20)
    spacing = 10
    val nextToRead: ObservableBuffer[PrioritizedBook] =
      ObservableBuffer[PrioritizedBook](for (book <-
                                             organizer.getFirst(5)) yield book)

    val tablePlaceholder = new VBox {
      padding = Insets(20, 10, 20, 0)
      spacing = 10
      content = List(
        new Label("There aren't any books added to your list"),
        new Button("Add book")
      )
    }

    tablePlaceholder.setAlignment(Pos.CENTER)

    val buttons = new TilePane {
      padding = Insets(20, 10, 20, 0)
      content = List(
        new Button("Show all") {
          prefWidth = 100
          minWidth = 100
        },
        new Button("Add new one"),
        new Button("Weekly planner")
      )

    }
    buttons.setHgap(8)
    buttons.setVgap(10)

    content = List(
      new Label {
        text = "Next to be read:"
        font = new Font("Verdana", 20)
      },
      createPriorityBookTable(nextToRead, tablePlaceholder),
      buttons
    )
  }
  content = mainBox
  alignmentInParent = Pos.CENTER
}
