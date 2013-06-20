package hello

import scalafx.scene.control._
import scalafx.scene.layout.{TilePane, HBox, VBox}
import scalafx.geometry.Insets
import scalafx.event.ActionEvent
import javafx.scene.control.Dialogs
import scalafx.stage.Stage
import scalafx.Includes._
import domain.{Tag, Category, Author, Book}

import scala.collection.JavaConversions._


import scalafx.collections.ObservableBuffer
import java.util

class BookChangePage(book: Book, dialogStage: Stage, model: AppModel) extends ScrollPane {

  val bufferOfSelectedTags = ObservableBuffer(for( tag <- book.tags) yield tag.tag)
  val allPossibleTagsForAdding =  ObservableBuffer(for(
    tag <- model.db.getAllTags if !(bufferOfSelectedTags contains tag.tag) )
  yield tag.tag)

  val bookChangingForm = new VBox() {
    spacing = 10
    padding = Insets(10, 10, 10, 10)
    val titleSection = new HBox {
      spacing = 10
      val titleEdit = new TextField() {
        text = book.getTitle
      }
      content = List(
        new Label("Title: "),
        titleEdit
      )
    }

    val authorSection = new HBox {
      spacing = 10
      val authorEdit = new ComboBox[String] {
        items = model.namesOfAuthors
        editable = true
        selectionModel.value.select(book.getAuthor.getName)
      }
      content = List(
        new Label("Author: "),
        authorEdit
      )
    }

    val categorySection = new HBox {
      spacing = 10
      val categoryEdit = new ComboBox[String]() {
        items = model.namesOfAllCategories
        editable = true
        selectionModel.value.select(book.category.category)
      }
      content = List(
        new Label("Category: "),
        categoryEdit
      )
    }

    val tagSection = new VBox {
      spacing = 10
      val selectedTags = new Label {

        text = bufferOfSelectedTags.mkString(", ")
      }

      def updateTagsLabel() {
        selectedTags.text = bufferOfSelectedTags.mkString(", ")
      }

      val addTagButton = new Button("Add tag") {
        onAction = {
          e: ActionEvent => {
            val tag = newTagComboBox.value.value
            bufferOfSelectedTags += tag
            allPossibleTagsForAdding -= tag
            updateTagsLabel()
          }
        }
      }

      val newTagComboBox = new ComboBox[String]() {
        items = allPossibleTagsForAdding
        editable = true
        prefWidth = 100
      }

      val removeTagButton = new Button("Remove tag") {
        onAction = {
          e: ActionEvent => {
            val tag = oldTagComboBox.value.value
            bufferOfSelectedTags -= tag
            allPossibleTagsForAdding += tag
            updateTagsLabel()
          }
        }
      }

      val oldTagComboBox = new ComboBox[String]() {
        prefWidth = 100
        items = bufferOfSelectedTags
      }

      content = List(
        new HBox {
          spacing = 10
          content = List(
            new Label("Tags: "),
            selectedTags)
        },
        new HBox {
          spacing = 10
          content = List(
            newTagComboBox,
            addTagButton,
            oldTagComboBox,
            removeTagButton
          )
        }
      )
    }

    val descriptionSection = new HBox {
      spacing = 10
      val descriptionEdit = new TextArea() {
        text = book.description
        if (book.description.isEmpty) {
          text = "No description available, write some"
        }
        wrapText = true
        prefColumnCount = 26
        prefRowCount = 5
      }
      content = List(
        new Label("Description: "),
        descriptionEdit
      )
    }

    val decisionButtons = new TilePane {
      hgap = 8
      val updateButton = new Button("Update") {
        onAction = {
          e: ActionEvent => {
            val dialogResponse = Dialogs.showConfirmDialog(dialogStage,
              "Update a book",
              "Are you sure, that you want to update this book?", "Update confirmation")
            if (dialogResponse == Dialogs.DialogResponse.YES) {
              model.books -= book
              model.db.removeBook(book)
              val title = titleSection.titleEdit.text.value
              val authorName = authorSection.authorEdit.value.value
              val description: String = descriptionSection.descriptionEdit.text.value
              val categoryDescription = categorySection.categoryEdit.value.value
              val changedBook = new Book(title, new Author(authorName), book.getPathToContent,
                description, new Category(categoryDescription))
              val tags: util.ArrayList[Tag] = new util.ArrayList( for (tagText <- bufferOfSelectedTags)
              yield new Tag(tagText))
              changedBook.tags = tags
              model.books += changedBook
              model.db.addBook(changedBook)
              model.updateNamesOfAuthors()
              model.updateAllToRead()
              Dialogs.showInformationDialog(dialogStage, "Book was updated", "Update complete", "Book update info")
            }

          }
        }
      }

      val cancelButton = new Button("Cancel") {
        onAction = {
          e: ActionEvent =>
            dialogStage.close
        }
      }
      content = List(updateButton, cancelButton)
    }

    content = List(
      titleSection,
      authorSection,
      descriptionSection,
      tagSection,
      categorySection,
      decisionButtons
    )
  }

  margin = Insets(10, 10, 10, 10)
  prefWidth = 500
  prefHeight = 300
  content = bookChangingForm
}
