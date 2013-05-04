package hello

import analysis.{Category, CategoryTree}
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
import scalafx.collections.ObservableBuffer
import scalafx.scene.text.Font

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
          content = createTableOfBooks()
          closable = false
        },
        new Tab {
          text = "Authors"
          content = createAccordionOfAuthors()
          closable = false
        },
        new Tab {
          text = "Categories"
          content = createTreeOfCategories()
          closable = false
        },
        new Tab {
          text = "Add a book"
          content = createAddingFormForBooks()
          closable = false
        }
      )
    }
  }

  private def createTableOfBooks(): Node = {
    val titleColumn = new TableColumn[Book, String]("Title") {
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

  private def createAccordionOfAuthors(): Node = new Accordion {
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
  
  def createCategoryNode(cat: Category): TreeItem[String] = {
  	new TreeItem(cat.value) {
            children = (for(subcatName <- cat.namesOfSubcategories)
            	yield createCategoryNode(cat(subcatName)) ).toList
          }
  }
  
  def createNodeListForTree(): List[TreeItem[String]] = {
  	val categories = model.categories
  	(for(subcatName <- categories.namesOfSubcategories)
            	yield createCategoryNode(categories(subcatName)) ).toList
  	
  }
  
  private def createTreeOfCategories(): Node = {
    val treeView = new TreeView[String] {
      minWidth = 150
      showRoot = false
      editable = false
      root = new TreeItem[String] {
        value = "Root"
        children = createNodeListForTree()
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
          val books = model.db.findBooksByCategory(newTreeItem.getValue)
          model.listViewItems += "Books in category: " + newTreeItem.getValue
          if(books.isEmpty)
          	model.listViewItems += "No books found"
          else {
          	for(book <- books) {
          		model.listViewItems += book.getTitle + " by " + book.getAuthor().getName()
          	}
          }
        }
      }
    )

    new SplitPane {
      items.addAll(
        treeView,
        listView
      )
    }
  }

  def createAddingFormForBooks(): Node = {
    val radioToggleGroup = new ToggleGroup()
    val variousControls = new VBox {
      padding = Insets(10)
      spacing = 20
      content = List(
      		new Label {
      			text = "Adding book form"
      			font = new Font("Verdana", 20)
      		},
      		new HBox {
          spacing = 10
          content = List(
            new Label("Choose file") ,
            new Button("Choose file") {
              onAction = {e: ActionEvent => println("Don't you have enough books?")}
            }
          )
        },
        new Label {
      			text = "Book data"
      			font = new Font("Verdana", 16)
      	},
        new TextField {
          promptText = "Title"
          prefColumnCount = 16
          text.onChange {println("TextField text is: " + text())}
        },
        new HBox {
          spacing = 10
          content = List(
        		  new Label {
        			  text = "Description"
        		  },
        		  new TextArea {
        			  prefColumnCount = 20
        					  prefRowCount = 2
        					  text.onChange {
        			  			println("TextArea text is: " + text())}
        		  			}
            )
        } ,
        new Label {
      			text = "Author data"
      			font = new Font("Verdana", 16)
      	},
        new CheckBox("Use already added author") {
          inner =>
          onAction = {
            e: ActionEvent =>
              println(e.eventType + " occured on CheckBox, and `selected` property is: " + inner.selected())
          }
        },
        new ChoiceBox(ObservableBuffer(for (author <- model.db.getAllAuthors) yield author.getName)) {
          selectionModel().selectFirst()
          selectionModel().selectedItem.onChange(
            (_, _, newValue) => println(newValue + " chosen in ChoiceBox")
          )
        },
        new TextField {
          promptText = "Name"
          prefColumnCount = 16
          text.onChange {println("TextField text is: " + text())}
        },
        new HBox {
          spacing = 10
          content = List(
            new Label {
              text = "Info"
            },
            new TextArea {
              prefColumnCount = 24
              prefRowCount = 2
              text.onChange {println("TextArea text is: " + text())}
            }
           )
        },
        new Button("Add to library") {
           onAction = {e: ActionEvent => println(e.eventType + " occured on Button")}
        }
         
      )
    }


    val sampleContextMenu = new ContextMenu {
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


