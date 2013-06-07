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

    val prioritySection = new HBox {
      spacing = 10
      val priorityLabel = new Label(priorityBook.priority.toString)
      content = List (
        new Label("Priority:"),
        priorityLabel,
        new Button("Increase"),
        new Button("Decrease")
      )
    }

    val progressSection = new HBox {
      spacing = 10
      content = List(
        new Label(priorityBook.progress.toString)
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
              priorityBook.title + " from your tasks?")
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
