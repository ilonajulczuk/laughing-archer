package hello

import scalafx.scene.layout.{HBox, TilePane, VBox}

import scalafx.stage.Stage
import scalafx.scene.control._
import scalafx.scene.text.Font
import domain.{Author, PrioritizedBook, BookOrganizer, Book}
import javafx.scene.control.TableColumn.CellDataFeatures
import scalafx.beans.property.StringProperty


import javafx.{util => jfxu}
import scalafx.Includes._
import javafx.beans.{value => jfxbv}
import scalafx.collections.{ObservableBuffer}
import scalafx.geometry.{Pos, Insets}
import scalafx.scene.Node
import scalafx.event.ActionEvent
import java.util.Date


class ReadingPlanView(model: AppModel, stage: Stage) extends ScrollPane {
  val organizer = new BookOrganizer

  var nextToRead: ObservableBuffer[PrioritizedBook] =
    ObservableBuffer[PrioritizedBook](for (book <-
                                           organizer.getFirst(5)) yield book)
  println(nextToRead)

  def updateBuffer(buffer: ObservableBuffer[PrioritizedBook], newContent: List[PrioritizedBook]) {
    buffer ++= newContent.toSet -- buffer.toSet
    buffer --= buffer.toSet -- newContent.toSet
  }

  def updateNextToRead() {
    val newList = organizer.getFirst(5)
    updateBuffer(nextToRead, newList)
    nextToRead.sort((lt, rt ) => lt.priority > rt.priority)
  }

  def updateListOfFreeBooksByRemovingAddedTitle(title: String) {
    model.busyBooks += title
    model.freeBooks -= title
  }
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
        new StringProperty(this, "Priority", param.getValue.priority.toString)
    })

    val deadlineColumn = new TableColumn[PrioritizedBook, String]("Deadline") {
      prefWidth = 180
    }
    deadlineColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Deadline", param.getValue.deadline.toString)
    })

    val progressColumn = new TableColumn[PrioritizedBook, String]("Progress") {
      prefWidth = 180
    }
    progressColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Progress", param.getValue.progress.toString)
    })

    val table = new TableView[PrioritizedBook](books) {
      prefHeight = 180
      prefWidth = 724

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
  var counter: Int = 0
  def addBook(e: ActionEvent) {

    StageUtil.showPageInWindow(addingBookPage(), "Add book", stage)

    println(nextToRead)
  }

  def addingBookPage() = {
    val form = new HBox() {

      padding = Insets(20, 10, 10, 20)
      spacing = 10
      val list = new ChoiceBox[String] {
        items = model.freeBooks
      }
      val priority = new ChoiceBox[Int] {
        items = ObservableBuffer[Int](0, 1, 2, 3, 4, 5)
      }

      content = List(
        new Label("Title:"),
        list,
        new Label("Priority:"),
        priority,
        //TODO deadline - date picker
        new Button("Add") {
          onAction = { e: ActionEvent =>
            val title: String = list.value.value
            val _priority = priority.value.value
            val deadline = new Date()
            val bookFromDB: Book = model.db.findBook(title)
            val prioritizedBook = new PrioritizedBook(bookFromDB, _priority, deadline)
            organizer.addBook(prioritizedBook)
            updateNextToRead()
            updateListOfFreeBooksByRemovingAddedTitle(title)
            println("Adding new book")
          }
        }
      )
    }

    val pane = new ScrollPane {
      prefHeight = 100
      prefWidth = 600
      content = form
    }
    pane

  }
  val mainBox = new VBox {
    padding = Insets(20, 10, 10, 20)
    spacing = 10


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
        new Button("Add new one") {
          onAction = addBook _
        },
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
      createPriorityBookTable(nextToRead),
      buttons
    )
  }
  content = mainBox
  alignmentInParent = Pos.CENTER
}

