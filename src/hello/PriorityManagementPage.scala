package hello

import domain.{PrioritizedBook}
import scalafx.stage.Stage
import scalafx.scene.layout.{TilePane, VBox}
import scalafx.geometry.Insets
import scalafx.scene.layout.HBox
import scalafx.event.ActionEvent
import javafx.scene.control.Dialogs

import scalafx.scene.control._

import scalafx.Includes._
import javafx.beans.property.{SimpleStringProperty}

/**
 * Created with IntelliJ IDEA.
 * User: att
 * Date: 6/7/13
 * Time: 2:03 PM
 */

class PriorityManagementPage(priorityBook: PrioritizedBook, dialogStage: Stage, model: AppModel) extends ScrollPane {
  val mainBox = new VBox {
    spacing = 10
    padding = Insets(20, 10, 10, 20)

    val priorityLabel = new Label(priorityBook.priority.toString)
    val progressNumber = new SimpleStringProperty(priorityBook.progress.toString)
    val prioritySection = new HBox {
      spacing = 10

      content = List (
        new Label("Priority:"),
        priorityLabel,
        new Button("Increase") {
          onAction = { e: ActionEvent => {
            val oldValue = priorityLabel.text.value.toInt
            println("Old value was: ", oldValue)
            val newValue: Int = min(oldValue + 1, 5).intValue()
            priorityLabel.text.value = newValue.toString
          }

          }
        },
        new Button("Decrease")  {
          onAction = { e: ActionEvent => {
            val oldValue = priorityLabel.text.value.toInt
            val newValue: Int = max(oldValue - 1, 0).intValue()
            priorityLabel.text.value = newValue.toString
          }

          }
        }
      )
    }

    val progressSection = new HBox {
      spacing = 10
      val progressLabel = new Label(priorityBook.progress.toString)
      progressLabel.text.bind(progressNumber)
      content = List(
       progressLabel,
        new CheckBox("done") {
          selected = false
          onAction = {
            e: ActionEvent =>
            {
              if(selected.value) {
                progressNumber.value = "100"
              }
              else {
                progressNumber.value = "0.0"
              }
            }
          }
        }
      )
    }

    def removeBook(book: PrioritizedBook) {
      model.organizer.removeBook(book)
      model.nextToRead -= book
      model.allToRead -= book
      model.db.removePrioritizedBook(book)
      model.busyBooks -= book.title
      model.freeBooks += book.title
    }

    val otherButtons = new TilePane {
      padding = Insets(20, 10, 20, 0)
      content = List(
        new Button("Delete") {
          onAction = {
            e: ActionEvent => {
              val confirmation = Dialogs.showConfirmDialog(dialogStage, "Do you really want to delete book " +
                priorityBook.title + " from your tasks?", "Delete task", "Deleting task")
              if(confirmation == Dialogs.DialogResponse.YES) {
                println("Removing book")
                removeBook(priorityBook)
                dialogStage.close
                Dialogs.showInformationDialog(dialogStage, "Book has been deleted", "Book deleted",
                  "Task to read this book was removed")
              }
            }
          }
          prefWidth = 100
          minWidth = 100

        },
        new Button("Postpone") {
          onAction = {
            e: ActionEvent => {
              Dialogs.showInformationDialog(dialogStage, "You postponed the task", "Task postponed",
                "Enjoying your procrastination")
            }
          }
        },
        new Button("Update") {
          onAction = {
            e: ActionEvent => {
              println("Updating...")
              val newPriority = priorityLabel.text.value.toInt
              val newProgress = progressNumber.value.toDouble

              if (newProgress == 0.0)  {
                println(newPriority, newProgress)

                model.organizer.changeBookPriority(priorityBook, newPriority )
                model.db.removePrioritizedBook(priorityBook)

                priorityBook.priority = newPriority

                model.updateNextToRead()
                model.updateAllToRead()
                model.db.addPrioritizedBook(priorityBook)
              }
              else {
                model.organizer.removeBook(priorityBook)
                Dialogs.showInformationDialog(new Stage(), "You completed reading book: " +
                  priorityBook.title, "Congratulation", "Task completed" )
                dialogStage.close
                model.updateNextToRead()
                model.updateAllToRead()
              }
            }}
        }
      )
    }

    content = List(
      new Label(priorityBook.title),
      prioritySection,
      new Label("Progress"),
      progressSection,
      otherButtons
    )
  }

  prefHeight = 200
  prefWidth = 524
  content = mainBox
}
