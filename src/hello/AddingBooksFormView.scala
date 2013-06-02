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
import domain.{Book, Author, Category, Tag}


class AddingBooksFormView(model: AppModel, stage: PrimaryStage) extends SplitPane {
  var usingAlreadyAddedAuthor = false
  var usingAlreadyAddedCategory = false
  val dialogStage = new Stage()
  val authors = new ComboBox[String]() {
    items = model.namesOfAuthors
    editable = true
  }

  val categories =  new ComboBox[String]() {
    items = model.namesOfAllCategories
    editable = true
  }

  val title = new TextField {
    promptText = "Title"
    text = ""
    prefColumnCount = 16

  }

  val addingBox = new VBox() {
    padding = Insets(20)
    spacing = 10
    content = List(
      new Button("Choose file") {
        onAction = chooseBookFile _
      },
      title,
      new HBox() {
        spacing = 10
        content = List(
          new Label("Author"),
          authors
        )
      },
      new HBox() {
        spacing = 10
        content = List(
          new Label("Category"),
          categories
        )
      },
      new Button("Add") {
        onAction = addBook _
      }

    )
  }

  def addBook(e: ActionEvent) {
    println("Adding book")
    val emptyFields = getEmptyFields()
    if (!emptyFields.isEmpty) {
      val capitalizedEmptyFields = emptyFields(0)(0).toUpper +
        emptyFields(0).tail :: emptyFields.tail

      Dialogs.showWarningDialog(dialogStage, capitalizedEmptyFields.mkString(", ") + " cannot be empty.",
        "Book couldn't be added.", "Adding failure")
    }
    else {
      val author = new Author(authors.value.value)
      val category = new Category(categories.value.value)
      val book = new Book(title.text.value, author, model.filePath, "", category)
      model.db.addBook(book)
      Dialogs.showInformationDialog(dialogStage, "You successfully added book to the library",
        "Book added.", "Adding complete")
      model.updateBooks()
      model.updateNamesOfAuthors()
      model.namesOfAllCategories += book.category.category
      model.storeBookTextOnDisk(book.getTitle, book.category.category)
    }
  }

  def getEmptyFields(): List[String] = {
    var emptyFields = List[String]()
    if (title.text.value == "")
      emptyFields = "title" :: emptyFields
    if (authors.value.value == "")
      emptyFields = "name of the author" :: emptyFields
    if (categories.value.value == "")
      emptyFields = "category" :: emptyFields
    emptyFields
  }

  def chooseBookFile(e: ActionEvent) {
    val fileChooser = new FileChooser()
    val result = fileChooser.showOpenDialog(dialogStage)
    if (result != null) {
      val path = result.getAbsolutePath
      if (model.isBookFormatSupported(path)) {
        model.updateBookFormatFromPath(path)
        dialogStage.close
        model.updateBookText(path)
        model.updatePreviewOfBookText()
        model.file = result
        model.filePath = path
      }
      else {
        Dialogs.showWarningDialog(dialogStage, "Book format: " +
          model.getBookFormatFromPath(path) + " is not supported",
          "Book couldn't be added.", "Adding failure")
      }
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
