package hello

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.scene.control._
import scalafx.scene.input.KeyCombination
import scalafx.scene.layout.{VBox, BorderPane}
import scalafx.scene.{Node, Scene}
import scalafx.application.JFXApp.PrimaryStage
import javafx.stage.WindowEvent
import javafx.event.EventHandler

object BigApp extends JFXApp {

  private val model = new AppModel()
  model.updateWorkspacePath()
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

  private def createTabs(): TabPane = {
    new TabPane {
      tabs = List(
        new Tab {
          text = "All books"
          content = createTableOfBooks()
          closable = false
        },
        new Tab {
          text = "Add a book"
          content = createAddingFormForBooks()
          closable = false
        },
        new Tab {
          text = "Parse a mobi"
          content = createMobiParsingView()
          closable = false
        },
        new Tab {
          text = "Preferences"
          content = createPreferencesView()
          closable = false
        },
        new Tab {
          text = "Reading Plan"
          content = createReadingPlanView()
          closable = false
        }
      )
    }
  }

  private def createPreferencesView(): Node = {
    new PreferencesView(model, stage)
  }

  private def createReadingPlanView(): Node = {
    new ReadingPlanView(model, stage)
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


