package hello

import scalafx.scene.{Scene, Node}
import scalafx.stage.{Modality, Stage}


object StageUtil {
  def showPageInWindow(page: Node, title: String, baseStage: Stage, dialogStage: Stage = new Stage()) {
    dialogStage.setTitle(title)
    dialogStage.initModality(Modality.WINDOW_MODAL)
    dialogStage.initOwner(baseStage)
    val scene = new Scene()
    scene.content = page
    dialogStage.setScene(scene)
    dialogStage.show
  }
}
