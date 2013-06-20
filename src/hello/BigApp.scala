package hello

import scalafx.application.JFXApp
import scalafx.scene.control._
import scalafx.scene.layout.{VBox, BorderPane}
import scalafx.scene.{Node, Scene}
import scalafx.application.JFXApp.PrimaryStage

object BigApp extends JFXApp {

  private val model = new AppModel()
  def createScene() = {
    new Scene(900, 600) {
      root = new BorderPane {
        top = new VBox {
          center = createTabs()
        }
      }
    }
  }

  stage = new PrimaryStage {
    scene = createScene()
    title = "Laughing archer"
  }

  private def createTabs(): TabPane = {
    new TabPane {
      tabs = List(
        new Tab {
          text = "Add a book"
          content = createAddingFormForBooks()
          closable = false
        },
        new Tab {
          text = "Parse mobi"
          content = createMobiParsingView()
          closable = false
        },
        new Tab {
          text = "All books"
          content = createTableOfBooks()
          closable = false
        },
        new Tab {
          text = "Reading Plan"
          content = createReadingPlanView()
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


