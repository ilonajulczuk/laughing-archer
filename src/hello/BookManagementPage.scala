package hello

import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.Insets
import scalafx.event.ActionEvent
import javafx.scene.control.Dialogs
import scalafx.stage.Stage

import scalafx.Includes._
import domain.Book

class BookManagementPage(book: Book, dialogStage: Stage, model: AppModel) extends ScrollPane {
  val bookSummaryText = book.detailedDescription()

  val summary = new TextArea {
    text = bookSummaryText
    wrapText = true
    prefColumnCount = 26
    prefRowCount = 10
  }

  val management = new VBox {
    padding = Insets(10)
    spacing = 10
    margin = Insets(10, 10, 10, 10)
    val bookChangingForm = new VBox {
      spacing = 10
      val titleEdit = new TextField() {
        text = book.getTitle
      }
      val authorEdit = new TextField() {
        text = book.getAuthor.getName
      }
      val descriptionEdit = new TextArea() {
        text = book.description
        wrapText = true
        prefColumnCount = 26
        prefRowCount = 5
      }

      val categoryEdit = new ComboBox[String]() {
        items = model.namesOfAllCategories
      }

      content = List(
        new Label("U mad?"),
        titleEdit,
        authorEdit,
        descriptionEdit,
        categoryEdit,
        new Button("Update") {
          onAction = {
            e: ActionEvent => {
              val dialogResponse = Dialogs.showConfirmDialog(dialogStage,
                "Update a book",
                "Are you sure, that you want to update this book?", "Update confirmation")
              if (dialogResponse == Dialogs.DialogResponse.YES) {
                //TODO implement real updating instead of adding book which was already there
                model.books += book
                model.db.addBook(book)
                model.updateNamesOfAuthors()
                Dialogs.showInformationDialog(dialogStage, "Book was updated", "Update complete", "Book update info")
              }

            }
          }
        }
      )
    }
    bookChangingForm.visible = false
    content = List(
      new HBox {
        spacing = 10
        content = List(
          summary,
          new VBox {
            spacing = 10
            content = List(
              new Button("Remove") {
                onAction = {
                  e: ActionEvent => {
                    val dialogResponse = Dialogs.showConfirmDialog(dialogStage,
                      "Remove a book",
                      "Are you sure, that you want to remove this book?", "Remove confirmation")
                    if (dialogResponse == Dialogs.DialogResponse.YES) {
                      model.db.removeBook(book)
                      model.books.remove(book)
                      model.updateNamesOfAuthors()
                      Dialogs.showInformationDialog(dialogStage, "Book was removed", "Removal complete", "Book removal info")
                      dialogStage.close
                    }

                  }
                }
              },
              new Button("Change") {
                onAction = {
                  e: ActionEvent => {
                    bookChangingForm.visible = true
                  }
                }
              }
            )
          }
        )
      },
      bookChangingForm
    )
  }

  margin = Insets(10, 10, 10, 10)
  prefWidth = 500
  prefHeight = 280
  content = management
}
