package hello

import scalafx.scene.control.{ScrollPane, Button, Label}
import scalafx.event.ActionEvent
import scalafx.stage.{Stage, DirectoryChooser}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.Insets

import scalafx.Includes._


class PreferencesView(model: AppModel, stage: Stage) extends ScrollPane {

    val pathToLibrary = new Label {
      text = model.libraryPath.getValue
    }

    pathToLibrary.text.bind(model.libraryPath)

    val changeLibraryPathButton = new Button("Change") {
      onAction = {
        e: ActionEvent =>
          val chooser = new DirectoryChooser()
          val result = chooser.showDialog(new Stage())

          if (result != null) {
            model.libraryPath.value = result.getAbsolutePath
            model setLibraryPath result.getAbsolutePath
          }
      }
    }

    val workspacePath = new Label {
      text = model.workspacePath.getValue
    }
    workspacePath.text.bind(model.workspacePath)

    val changeWorkspacePathButton = new Button("Change") {
      onAction = {
        e: ActionEvent => {
          println(e.eventType + " occurred on MenuItem New")
          val chooser = new DirectoryChooser()
          val result = chooser.showDialog(new Stage())
          if (result != null) {
            model.workspacePath.value = result.getAbsolutePath
            model.setWorkspacePath(result.getAbsolutePath)
          }
        }
      }
    }

  content = new VBox {
    spacing = 10
    margin = Insets(20, 10, 10, 20)
    content = List(
      new HBox {
        spacing = 10
        margin = Insets(10, 0, 0, 20)
        content = List(
          new Label("Path to library:") {
          },
          pathToLibrary,
          changeLibraryPathButton
        )
      },
      new HBox {
        spacing = 10
        margin = Insets(0, 0, 0, 20)
        content = List(
          new Label("Path to workspace:") {
          },
          workspacePath,
          changeWorkspacePathButton
        )
      }
    )
  }
}
