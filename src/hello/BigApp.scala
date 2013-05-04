package hello

import scalafx.beans.property.StringProperty
import mobireader.Book
import javafx.beans.{value => jfxbv}
import javafx.scene.control.TableColumn.CellDataFeatures
import javafx.{event => jfxe}
import javafx.{util => jfxu}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.geometry.{Pos, Orientation, Insets}
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.KeyCombination
import scalafx.scene.layout.{HBox, StackPane, VBox, BorderPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Rectangle, Circle}
import scalafx.scene.web.{HTMLEditor, WebView}
import scalafx.scene.{Node, Scene}
import scalafx.stage.{Stage, Popup}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.control.ScrollPane.ScrollBarPolicy


/**
 * @author Jarek Sacha
 */
object BigMain extends JFXApp {

  private val model = new AppModel()
    
  stage = new PrimaryStage {
    scene = new Scene(900, 600) {
      root = new BorderPane {
        top = new VBox {
          content = List(
            createMenus()
          )
          center = createTabs()
        }
      }
    }
    title = "Laughing archer"
  }


  private def createMenus() = new MenuBar {
    menus = List(
      new Menu("File") {
        items = List(
          new MenuItem("New...") {
            accelerator = KeyCombination.keyCombination("Ctrl +N")
            onAction = {e: ActionEvent => println(e.eventType + " occurred on MenuItem New")}
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
          content = createTableDemoNode()
          closable = false
        },
        new Tab {
          text = "Authors"
          content = createAccordionTitledDemoNode()
          closable = false
        },
        new Tab {
          text = "Categories"
          content = createSplitTreeListDemoNode()
          closable = false
        },
        new Tab {
          text = "Add a book"
          content = createScrollMiscDemoNode()
          closable = false
        }
      )
    }
  }

  private def createTableDemoNode(): Node = {
    val titleColumn = new TableColumn[Book, String]("First Name") {
      prefWidth = 180
    }
    titleColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[Book, String]) = new StringProperty(this, "Title", param.getValue.getTitle)
    })

    val authorColumn = new TableColumn[Book, String]("Author name") {
      prefWidth = 180
    }
    authorColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[Book, String]) =  
      	new StringProperty(this, "AuthorName", param.getValue.getAuthor.getName())
    })

    val descriptionColumn = new TableColumn[Book, String]("Description") {
      prefWidth = 180
    }
    descriptionColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[Book, String]) = 
    		  new StringProperty(this, "Description", param.getValue.description)
    })
    
    val pathColumn = new TableColumn[Book, String]("Path") {
      prefWidth = 180
    }
    pathColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[Book, String]) = 
    		  new StringProperty(this, "Path", param.getValue.getPathToContent)
    })
    
    val categoryColumn = new TableColumn[Book, String]("Category") {
      prefWidth = 180
    }
    categoryColumn.setCellValueFactory(new jfxu.Callback[CellDataFeatures[Book, String], jfxbv.ObservableValue[String]] {
      def call(param: CellDataFeatures[Book, String]) = 
    		  new StringProperty(this, "Category", param.getValue.category)
    })

    val table = new TableView[Book](model.getBooks) {
      // NOTE: there may be an issue with assigning columns directly, do it through delegate
      //      columns ++= List(
      //        tc
      //      )
      delegate.getColumns.addAll(
        titleColumn.delegate,
        authorColumn.delegate,
        descriptionColumn.delegate,
        pathColumn.delegate,
        categoryColumn.delegate
      )
    }
    table.getSelectionModel.selectedItemProperty.onChange(
      (_, _, newValue) => println(newValue + " chosen in TableView")
    )
    table
  }

  private def createAccordionTitledDemoNode(): Node = new Accordion {
    panes = createAccordionViewOfAuthors
    expandedPane = panes.head
  }

  def createAccordionViewOfAuthors() : List[TitledPane] = {
  	val authors = model.db.getAllAuthors
  	for(author <- authors) yield new TitledPane {
        text = author.getName
        content = new TextArea {
          text = author.provideAllInfo()
        }
      }
  	
  }
  
  /*def createNodeListForTree(): Node = {
  	val categories = model.categories
  	
  }*/
  
  private def createSplitTreeListDemoNode(): Node = {
    val treeView = new TreeView[String] {
      minWidth = 150
      showRoot = false
      editable = false
      root = new TreeItem[String] {
        value = "Root"
        children = List(
          new TreeItem("Animal") {
            children = List(
              new TreeItem("Lion"),
              new TreeItem("Tiger"),
              new TreeItem("Bear")
            )
          },
          new TreeItem("Mineral") {
            children = List(
              new TreeItem("Coper"),
              new TreeItem("Diamond"),
              new TreeItem("Quartz")
            )
          },
          new TreeItem("Vegetable") {
            children = List(
              new TreeItem("Arugula"),
              new TreeItem("Broccoli"),
              new TreeItem("Cabbage")
            )
          }
        )
      }
    }

    val listView = new ListView[String] {
      items = model.listViewItems
    }

    treeView.selectionModel().setSelectionMode(SelectionMode.SINGLE)
    treeView.selectionModel().selectedItem.onChange(
      (_, _, newTreeItem) => {
        if (newTreeItem != null && newTreeItem.isLeaf) {
          model.listViewItems.clear()
          for (i <- 1 to 10000) {
            model.listViewItems += newTreeItem.getValue + " " + i
          }
        }
      }
    )

    new SplitPane {
      // NOTE: Using JavaFX way of adding items using `addAll`.
      //      items = List (
      //          treeView,
      //          listView
      //      )
      items.addAll(
        treeView,
        listView
      )
    }
  }


  def createScrollMiscDemoNode(): Node = {
    val radioToggleGroup = new ToggleGroup()
    val variousControls = new VBox {
      padding = Insets(10)
      spacing = 20
      content = List(
        new Button("Button") {
          onAction = {e: ActionEvent => println(e.eventType + " occured on Button")}
        },
        new CheckBox("CheckBox") {
          inner =>
          onAction = {
            e: ActionEvent =>
              println(e.eventType + " occured on CheckBox, and `selected` property is: " + inner.selected())
          }
        },
        new HBox {
          spacing = 10
          content = List(
            new RadioButton("RadioButton1") {
              toggleGroup = radioToggleGroup
            },
            new RadioButton("RadioButton2") {
              toggleGroup = radioToggleGroup
            }
          )
        },
        new Hyperlink("Hyperlink") {
          onAction = {e: ActionEvent => println(e.eventType + " occurred on Hyperlink")}
        },
        new ChoiceBox(model.choiceBoxItems) {
          selectionModel().selectFirst()
          selectionModel().selectedItem.onChange(
            (_, _, newValue) => println(newValue + " chosen in ChoiceBox")
          )
        },
        new MenuButton("MenuButton") {
          items = List(
            new MenuItem("MenuItem A") {
              onAction = {ae: ActionEvent => println(ae.eventType + " occurred on Menu Item A")}
            },
            new MenuItem("MenuItem B")
          )
        },
        new SplitMenuButton {
          text = "SplitMenuButton"
          onAction = {ae: ActionEvent => println(ae.eventType + " occurred on SplitMenuButton")}
          items = List(
            new MenuItem("MenuItem A") {
              onAction = {ae: ActionEvent => println(ae.eventType + " occurred on Menu Item A")}
            },
            new MenuItem("MenuItem B")
          )
        },
        new TextField {
          promptText = "Enter user name"
          prefColumnCount = 16
          text.onChange {println("TextField text is: " + text())}
        },
        new PasswordField {
          promptText = "Enter password"
          prefColumnCount = 16
          text.onChange {println("PasswordField text is: " + text())}
        },
        new HBox {
          spacing = 10
          content = List(
            new Label {
              text = "TextArea"
            },
            new TextArea {
              prefColumnCount = 12
              prefRowCount = 4
              text.onChange {println("TextArea text is: " + text())}
            }
          )
        }
      )
    }

    radioToggleGroup.selectToggle(radioToggleGroup.toggles(0))
    radioToggleGroup.selectedToggle.onChange {
      val rb = radioToggleGroup.selectedToggle.get.asInstanceOf[javafx.scene.control.ToggleButton]
      if (rb != null) println(rb.id() + " selecled")
    }

    val sampleContextMenu = new ContextMenu {
      // NOTE: Adding menu items through delegate. Adding directly does nothing.
      //      items ++= List(
      //        new MenuItem("MenuItemA") {
      //          onAction = {e: ActionEvent => println(e.eventType + " occurred on Menu Item A")}
      //        },
      //        new MenuItem("MenuItemB") {
      //          onAction = {e: ActionEvent => println(e.eventType + " occurred on Menu Item B")}
      //        }
      //      )
      delegate.getItems.addAll(
        new MenuItem("MenuItemA") {
          onAction = {e: ActionEvent => println(e.eventType + " occurred on Menu Item A")}
        }.delegate,
        new MenuItem("MenuItemB") {
          onAction = {e: ActionEvent => println(e.eventType + " occurred on Menu Item B")}
        }.delegate
      )
    }

    new ScrollPane {
      content = variousControls
      hbarPolicy = ScrollBarPolicy.ALWAYS
      vbarPolicy = ScrollBarPolicy.AS_NEEDED
      contextMenu = sampleContextMenu
    }
  }

  def createHtmlEditorDemoNode(): Node = {

    val htmlEditor = new HTMLEditor {
      htmlText = "<p>Replace this text</p>"
    }

    val viewHTMLButton = new Button("View HTML") {
      onAction = {
        e: ActionEvent => {
          val alertPopup = createAlertPopup(htmlEditor.htmlText)
          alertPopup.show(stage,
            (stage.width() - alertPopup.width()) / 2.0 + stage.x(),
            (stage.height() - alertPopup.height()) / 2.0 + stage.y())
        }
      }
      alignment = Pos.CENTER
      margin = Insets(10, 0, 10, 0)
    }

    new BorderPane {
      center = htmlEditor
      bottom = viewHTMLButton
    }
  }


  def createAlertPopup(popupText: String) = new Popup {
    inner =>
    content.add(new StackPane {
      content = List(new Rectangle {
        width = 300
        height = 200
        arcWidth = 20
        arcHeight = 20
        fill = Color.LIGHTBLUE
        stroke = Color.GRAY
        strokeWidth = 2
      },
        new BorderPane {
          center = new Label {
            text = popupText
            wrapText = true
            maxWidth = 280
            maxHeight = 140
          }
          bottom = new Button("OK") {
            onAction = {e: ActionEvent => inner.hide}
            alignment = Pos.CENTER
            margin = Insets(10, 0, 10, 0)
          }
        }
      )
    }.delegate
    )
  }
}


