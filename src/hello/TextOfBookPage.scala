package hello

import scalafx.scene.control.{TextArea, ScrollPane}
import scalafx.scene.layout.VBox

/**
 * Created with IntelliJ IDEA.
 * User: att
 * Date: 6/10/13
 * Time: 11:30 AM
 */
class TextOfBookPage(textToShow: String) extends ScrollPane {
  val bookText = new TextArea {
    text = textToShow
    wrapText = true
    editable = false
  }
  val mainBox = new VBox {
    content = bookText
    prefHeight = 590
    prefWidth = 442
  }

  bookText.prefHeight.bind(mainBox.prefHeightProperty)
  bookText.prefWidth.bind(mainBox.prefWidthProperty)

  prefHeight = 600
  prefWidth = 450
  content = mainBox
}
