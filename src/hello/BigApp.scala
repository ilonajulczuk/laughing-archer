package hello

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.scene.control._
import scalafx.scene.input.KeyCombination
import scalafx.scene.layout.{VBox, BorderPane}
import scalafx.scene.{Node, Scene}
import scalafx.application.JFXApp.PrimaryStage


object BigApp extends JFXApp {

  private val model = new AppModel()
  var tableOfBooks = createTableOfBooks()
  var addingBookForm = createAddingFormForBooks()
  var mobiParsingView = createMobiParsingView()

  def createScene() = {
    new Scene(900, 600) {
      root = new BorderPane {
        top = new VBox {
          content = List(
            createMenus()
          )
          center = createTabs()
        }
      }
    }
  }

  stage = new PrimaryStage {
    scene = createScene()
    title = "Laughing archer"
  }

  private def createMenus() = new MenuBar {
    menus = List(
      new Menu("File") {
        items = List(
          new MenuItem("New...") {
            accelerator = KeyCombination.keyCombination("Ctrl + N")
            onAction = {
              e: ActionEvent => println(e.eventType + " occurred on MenuItem New")
            }
          },
          new MenuItem("Save")
        )
      },
      new Menu("Edit") {
        items = List(
          new MenuItem("Cut"),
          new MenuItem("Copy"),
          new MenuItem("Paste")
        )
      }
    )
  }

  val lib = model.getLibraryPath
  if (lib != null) {
    model.libraryPath.value = model.getLibraryPath
    stage.setTitle("Laughing archer with library at - " + lib)
  }
  val work = model.getWorkspacePath
  if (work != null)
    model.workspacePath.value = model.getWorkspacePath

  private def createTabs(): TabPane = {
    new TabPane {
      tabs = List(
        new Tab {
          text = "All books"
          content = tableOfBooks
          closable = false
        },
        new Tab {
          text = "Add a book"
          content = addingBookForm
          closable = false
        },
        new Tab {
          text = "Parse a mobi"
          content = mobiParsingView
          closable = false
        },
        new Tab {
          text = "Preferences"
          content = createPreferencesView()
          closable = false
        }
      )
    }
  }

  private def createPreferencesView(): Node = {
    new PreferencesView(model, stage)
  }

  private def createReadingPlanView(): Node = {
    new ReadingPlanView()

  }

  private def createMobiParsingView(): Node = {
    new MobiParsingView(model, stage)
  }

  private def createTableOfBooks(): Node = {
    new TableOfBooksView(model, stage).table
  }

  def createAddingFormForBooks(): Node = {
    new AddingBooksFormView(model, stage)
  }
}


