package hello

import scalafx.scene.layout.{HBox, TilePane, VBox}

import scalafx.stage.Stage
import scalafx.scene.control._
import scalafx.scene.Node
import scalafx.scene.text.Font
import domain.{PrioritizedBook, Book}
import javafx.scene.control.TableColumn.CellDataFeatures
import scalafx.beans.property.StringProperty


import javafx.{util => jfxu}
import scalafx.Includes._
import javafx.beans.{value => jfxbv}
import scalafx.collections.{ObservableBuffer}
import scalafx.geometry.{Pos, Insets}
import scalafx.event.ActionEvent
import java.util.{ Date}
import javafx.scene.control.Dialogs
import org.joda.time.DateTime
import java.text.SimpleDateFormat


class ReadingPlanView(model: AppModel, stage: Stage) extends ScrollPane {



    println(model.nextToRead)

  def updateBuffer(buffer: ObservableBuffer[PrioritizedBook], newContent: List[PrioritizedBook]) {
    buffer ++= newContent.toSet -- buffer.toSet
    buffer --= buffer.toSet -- newContent.toSet
  }



  def updateListOfFreeBooksByRemovingAddedTitle(title: String) {
    model.busyBooks += title
    model.freeBooks -= title
  }

  def createPriorityBookTable(books: ObservableBuffer[PrioritizedBook], preferredHeight: Int = 180) : TableView[PrioritizedBook] = {
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

    val dateFormat = new SimpleDateFormat("dd/MM/yyyy")

    val deadlineColumn = new TableColumn[PrioritizedBook, String]("Deadline") {
      prefWidth = 180
    }
    deadlineColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Deadline", dateFormat.format(param.getValue.deadline))
    })

    val progressColumn = new TableColumn[PrioritizedBook, String]("Progress") {
      prefWidth = 180
    }
    progressColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[PrioritizedBook, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[PrioritizedBook, String]) =
        new StringProperty(this, "Progress", param.getValue.progress.toString)
    })

    def createPriorityManagementPage(book: PrioritizedBook, dialogStage: Stage) = {
      new PriorityManagementPage(book, dialogStage, model)
    }

    def showPriorityBookManagement(book: PrioritizedBook) {
      val dialogStage = new Stage()
      val page = createPriorityManagementPage(book, dialogStage)
      StageUtil.showPageInWindow(page, "Book Management", dialogStage)
    }

    val table = new TableView[PrioritizedBook](books) {
      prefHeight = preferredHeight
      prefWidth = 724

      delegate.getColumns.addAll(
        titleColumn.delegate,
        priorityColumn.delegate,
        deadlineColumn.delegate,
        progressColumn.delegate
      )
    }
    table.getSelectionModel.selectedItemProperty.onChange(
      (_, oldValue, newValue) => {

        if(oldValue == null && newValue != null)
          showPriorityBookManagement(newValue)

      }
    )
    table
  }
  var counter: Int = 0
  def addBook(e: ActionEvent) {

    val showingStage = new Stage
    StageUtil.showPageInWindow(addingBookPage(showingStage), "Add book", stage, showingStage)

    println(model.nextToRead)
  }

  def allBookPage() = {
    model.updateAllToRead()

    val table = createPriorityBookTable(model.allToRead, 725)
    val pane = new ScrollPane {
      prefHeight = 480
      prefWidth = 730
      content = table
    }
    pane
  }

  def addingBookPage(stage: Stage) = {
    val form = new VBox() {

      padding = Insets(20, 10, 10, 20)
      spacing = 10
      val list = new ChoiceBox[String] {
        items = model.freeBooks
      }
      list.selectionModel.value.selectFirst()
      val priority = new ChoiceBox[Int] {
        items = ObservableBuffer[Int](0, 1, 2, 3, 4, 5)
      }
      priority.selectionModel.value.selectFirst()

      def createDateBasedOnDescription(description: String):Date = {
        val today: DateTime = new DateTime()
        println("Description is: " + description)
        val result = description match {
          case "today" =>  today
          case "tomorrow" => today.plusDays(1)
          case "next five days" => today.plusDays(5)
          case "next week" => today.plusWeeks(1)
          case "month from now" => today.plusMonths(1)
          case "year from now" => today.plusYears(1)
          case "far future" => today.plusYears(20)
        }
        result.toDate
      }

      val datePickingSection = new HBox {
        spacing = 10
        val dateChoiceBox = new ChoiceBox[String] {
          items = ObservableBuffer("today", "tomorrow", "next five days", "next week", "month from now",
            "year from now", "far future" )
          selectionModel.value.selectFirst()
        }

        content = List[Node](
          new Label("Deadline:"),
          dateChoiceBox
        )
      }
      content = List(
        new HBox {
          spacing = 10
          content = List(
            new Label("Title:"),
            list,
            new Label("Priority:"),
            priority
          )
        }
        ,
        datePickingSection,
        new Button("Add") {
          onAction = { e: ActionEvent =>
            val title: String = list.value.value
            val _priority = priority.value.value
            if(model.freeBooks.isEmpty) {
              Dialogs.showWarningDialog(new Stage, "No books left", "No books are left", "No books left")
            }
            else {
              if(title == "") {
                Dialogs.showWarningDialog(new Stage, "Adding error", "Specify what you want to add first",
                  "Book not added")
              }
              else {
                val deadline = createDateBasedOnDescription(datePickingSection.dateChoiceBox.value.value)
                val bookFromDB: Book = model.db.findBook(title)
                val prioritizedBook = new PrioritizedBook(bookFromDB, _priority, deadline)

                model.organizer.addBook(prioritizedBook)
                model.updateNextToRead()
                updateListOfFreeBooksByRemovingAddedTitle(title)
                model.db.addPrioritizedBook(prioritizedBook)

                stage.close
                println("Updating next to read")
                model.updateNextToRead()
              }
            }
          }
        }
      )
    }

    val pane = new ScrollPane {
      prefHeight = 130
      prefWidth = 500
      content = form
    }
    pane

  }
  val mainBox = new VBox {
    padding = Insets(20, 10, 10, 20)
    spacing = 10

    val buttons = new TilePane {
      padding = Insets(20, 10, 20, 0)
      content = List(
        new Button("Show all") {
          prefWidth = 100
          minWidth = 100
          onAction = {e: ActionEvent => {
            val page = allBookPage()
            StageUtil.showPageInWindow(page,"All books", stage)
          }}
        },
        new Button("Add new one") {
          onAction = addBook _
        }
      )
    }

    buttons.setHgap(8)
    buttons.setVgap(10)

    content = List(
      new Label {
        text = "Next to read:"
        font = new Font("Verdana", 20)
      },
      createPriorityBookTable(model.nextToRead),
      buttons
    )
  }
  content = mainBox
  alignmentInParent = Pos.CENTER
}

