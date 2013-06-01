package hello

import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.Insets
import scalafx.event.ActionEvent
import scalafx.application.JFXApp.PrimaryStage
import javafx.scene.control.Dialogs
import javafx.stage.FileChooser
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import scalafx.scene.text.Font
import scalafx.Includes._

import scalafx.scene.control._
import scalafx.stage.Stage
import domain.{Book, Author}


class AddingBooksFormView(model: AppModel, stage: PrimaryStage) extends SplitPane {
  var usingAlreadyAddedAuthor = false
  var usingAlreadyAddedCategory = false
  val addingBox = new VBox {
    padding = Insets(20)
    vgrow = scalafx.scene.layout.Priority.ALWAYS
    hgrow = scalafx.scene.layout.Priority.ALWAYS
    spacing = 10
    margin = Insets(50, 0, 0, 50)
    var chosenFileName = ""

    val bookTitle = new TextField {
      promptText = "Title"
      text = ""
      prefColumnCount = 16

    }

    val bookDescription = new TextArea {
      prefColumnCount = 20
      prefRowCount = 2

    }

    val useAuthor = new CheckBox("Use already added author") {
      inner =>
      onAction = {
        e: ActionEvent =>
          if (inner.selected()) {
            usingAlreadyAddedAuthor = true
            authorName.text.value = authorBox.value.value
            authorInfo.text.value = model.db.findAuthor(authorBox.value.value).additionalInfo
            authorName.editable = false
            authorInfo.editable = false
          }
          else {
            usingAlreadyAddedAuthor = false
            authorName.text.value = authorBox.value.value
            authorInfo.text.value = model.db.findAuthor(authorBox.value.value).additionalInfo
            authorName.editable = true
            authorInfo.editable = true
          }

      }
    }

    val filePath = new Label("Nothing selected")
    filePath.wrapText = true

    val authorBox = new ChoiceBox[String]() {
      items = model.namesOfAuthors
      selectionModel().selectFirst()
      selectionModel().selectedItem.onChange(
      {
        (_, _, newValue) => println(newValue + " chosen in ChoiceBox")
          if (usingAlreadyAddedAuthor) {
            authorName.text.value_=(newValue)
          }
      }
      )
    }

    val authorName = new TextField {
      promptText = "Name"
      prefColumnCount = 16
    }

    val authorInfo = new TextArea {
      prefColumnCount = 24
      prefRowCount = 2
    }

    val useCategory = new CheckBox("Use already added category") {
      inner =>
      onAction = {
        e: ActionEvent =>
          println {
            e.eventType + " occured on CheckBox, and `selected` property is: " + inner.selected()
          }
          usingAlreadyAddedCategory = inner.selected()
          if (usingAlreadyAddedCategory) {
            categoryName.text.value = categoryBox.value.value
            categoryName.editable = false
          }
          else
            categoryName.editable = true
      }
    }

    val categoryBox = new ChoiceBox[String] {
      maxWidth = 80
      minWidth = 80
      maxHeight = 50
      items = model.namesOfAllCategories
      selectionModel().selectFirst()
      selectionModel().selectedItem.onChange(
      {
        (_, _, newValue) => println(newValue + " chosen in ChoiceBox")
          if (usingAlreadyAddedCategory) {
            categoryName.text.value_=(newValue)
          }
      }
      )
    }

    val categoryName = new TextField {
      promptText = "Category"
      prefColumnCount = 16
    }

    content = List(
      new VBox {
        spacing = 10
        val button = new Button("Choose file")
        button.onAction =({
          (_: ActionEvent) =>
            try {
              val fileChooser = new FileChooser()
              val newStage = new Stage()
              val result = fileChooser.showOpenDialog(newStage)
              if (result != null) {
                val path = result.getAbsolutePath
                if (model.isBookFormatSupported(path)) {
                  if (bookTitle.text.value == "") bookTitle.text = model.getBookTitleFromPath(path)
                  filePath.text = model.normalizePath(path, 50)
                  model.updateBookFormatFromPath(path)
                  model.updateBookText(path)
                  model.updatePreviewOfBookText()
                  model.file = result
                  model.filePath = path
                }
                else {
                  Dialogs.showWarningDialog(stage, "Book format: " +
                    model.getBookFormatFromPath(path) + " is not supported",
                    "Book couldn't be added.", "Adding failure")
                }
              }

            }
        })
        content = List(filePath, button)
      },
      bookTitle
      ,
      new HBox {
        spacing = 10
        content = List(
          new Label {
            text = "Description"
          },
          bookDescription
        )
      },
      useAuthor,
      authorBox,
      authorName,
      new HBox {
        spacing = 10
        content = List(
          authorInfo
        )
      },
      useCategory,
      categoryBox,
      categoryName,
      new Button("Add to library") {
        onAction = {
          e: ActionEvent =>
            val emptyFields = getEmptyFields()

            if (!emptyFields.isEmpty) {
              val capitalizedEmptyFields = emptyFields(0)(0).toUpper +
                emptyFields(0).tail :: emptyFields.tail
              Dialogs.showWarningDialog(stage, capitalizedEmptyFields.mkString(", ") + " cannot be empty.",
                "Book couldn't be added.", "Adding failure")

            }
            else {
              val book: Book = createBookBasedOnForm()
              if (!model.books.contains(book)) {
                //TODO make it working
                model.db.addBook(book)
                assert(model.db.findBook(book.getTitle) != null,
                  "Just added book cannot be found")
                model.books += book
                model.namesOfAllCategories += book.category
                model.categories.addSubcategories(List(book.category))
                val author = book.getAuthor
                author.addTitle(book.getTitle)
                model.storeBookTextOnDisk(book.getTitle, book.category)
                val dialogStage = new Stage
                Dialogs.showInformationDialog(dialogStage, "You successfully added book to the library",
                  "Book added.", "Adding complete")
                model.updateNamesOfAuthors()
              }
            }
        }
      }
    )

    def getEmptyFields(): List[String] = {
      var emptyFields = List[String]()
      if (bookTitle.text.value == "")
        emptyFields = "title" :: emptyFields
      if (filePath.text.value == "")
        emptyFields = "path to file" :: emptyFields
      if (bookDescription.text.value == "")
        emptyFields = "book description" :: emptyFields
      if (authorName.text.value == "")
        emptyFields = "name of the author" :: emptyFields
      if (authorInfo.text.value == "")
        emptyFields = "info about the author" :: emptyFields
      if (categoryName.text.value == "")
        emptyFields = "category" :: emptyFields
      emptyFields
    }

    def createBookBasedOnForm(): Book = {
      val title = bookTitle.text.value
      val path = model.filePath
      val description = bookDescription.text.value
      val nameOfAuthor = authorName.text.value
      val additionalInfoAboutAuthor = authorInfo.text.value
      val category = categoryName.text.value
      val author = new Author(nameOfAuthor, additionalInfoAboutAuthor)
      new Book(title, author, path, description, category)
    }
  }

  val left = new ScrollPane {
    content = addingBox
    hbarPolicy = ScrollBarPolicy.AS_NEEDED
    vbarPolicy = ScrollBarPolicy.AS_NEEDED
  }

  val right = new ScrollPane {
    content = new VBox {
      padding = Insets(20)
      vgrow = scalafx.scene.layout.Priority.ALWAYS
      hgrow = scalafx.scene.layout.Priority.ALWAYS
      spacing = 10
      margin = Insets(50, 0, 0, 50)
      val previewTitle = new Label {
        text = "Book preview"
        font = new Font("Verdana", 20)
      }
      val previewText = new TextArea {
        text = "Content not available"
        wrapText = true
        editable = false
      }
      previewText.prefHeight.bind(left.prefHeightProperty)
      previewText.prefWidth.bind(left.prefWidthProperty)
      previewText.prefColumnCount = 28
      previewText.prefRowCount = 30
      previewText.text.bind(model.bookTextPreview)
      previewTitle.text.bind(addingBox.bookTitle.text)
      content = List(
        previewTitle,
        previewText
      )
    }
  }

  items.addAll(
    left,
    right
  )

}
