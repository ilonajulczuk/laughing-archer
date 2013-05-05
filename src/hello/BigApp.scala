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
import scalafx.stage.{Stage, Popup, FileChooser}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import scalafx.collections.ObservableBuffer
import scalafx.scene.text.Font
import javafx.{stage => jfxs}

object BigMain extends JFXApp {

	private val model = new AppModel()
	
	var tableOfBooks = createTableOfBooks()
	var accordionOfAuthors = createAccordionOfAuthors()
	var treeOfCategories = createTreeOfCategories()
	var addingBookForm = createAddingFormForBooks()
	
	var mainLayout = createTabs()
	def updateTabs() {
	  tableOfBooks = createTableOfBooks()
	  accordionOfAuthors = createAccordionOfAuthors()
	  treeOfCategories = createTreeOfCategories()
	  addingBookForm = createAddingFormForBooks()
	}
	
	
	def myRoot = new BorderPane {
				top = new VBox {
					content = List(
							createMenus()
							)
							center = mainLayout
				}
			}
	
	def myScene = new Scene(900, 600) {
			root = myRoot
		}
	
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
		scene = myScene 
		title = "Laughing archer"
	}
	
	
	
	private def createMenus() = new MenuBar {
		menus = List(
				new Menu("File") {
					items = List(
							new MenuItem("New...") {
								accelerator = KeyCombination.keyCombination("Ctrl + N")
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
									content = tableOfBooks
									closable = false
						},
						new Tab {
							text = "Authors"
									content = accordionOfAuthors
									closable = false
						},
						new Tab {
							text = "Categories"
									content = treeOfCategories
									closable = false
						},
						new Tab {
							text = "Add a book"
									content = addingBookForm
									closable = false
						}
						)
			}
	}

	private def createTableOfBooks(): Node = {
	  println("Creating tables...")
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
			val variousControls = new VBox {
				padding = Insets(20)
						vgrow = scalafx.scene.layout.Priority.ALWAYS
						hgrow = scalafx.scene.layout.Priority.ALWAYS
						spacing = 10
						margin = Insets(50, 0, 0, 50)
						var chosenFileName = ""

						val bookTitle = new TextField {
					promptText = "Title"
							prefColumnCount = 16
							text.onChange {println("TextField text is: " + text())}
				}

				val bookDescription = new TextArea {
					prefColumnCount = 20
							prefRowCount = 2
							text.onChange {
						println("TextArea text is: " + text())
					}
				}

				val useAuthor = new CheckBox("Use already added author") {
					inner =>
					onAction = { e: ActionEvent =>
					println(e.eventType + " occured on CheckBox, and `selected` property is: " + inner.selected())
					}
				}
				
				val filePath = new Label("Nothing selected")
				
				val authorBox = new ChoiceBox(ObservableBuffer(for (author <- model.db.getAllAuthors) yield author.getName)) {
					selectionModel().selectFirst()
					selectionModel().selectedItem.onChange(
							(_, _, newValue) => println(newValue + " chosen in ChoiceBox")
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
					onAction = { e: ActionEvent =>
					println(e.eventType + " occured on CheckBox, and `selected` property is: " + inner.selected())
					}
				}

				val categoryBox = new ChoiceBox[String] {
					maxWidth = 80
					maxHeight = 50
					items = model.namesOfAllCategories
					selectionModel().selectFirst()
					selectionModel().selectedItem.onChange(
							(_, _, newValue) => println(newValue + " chosen in ChoiceBox")
					)
				}
					
				

				val categoryName = new TextField {
					promptText = "Category"
							prefColumnCount = 16
				}
				content = List(
						new Label {
							text = "Adding book form"
									font = new Font("Verdana", 20)
						},
						
						new HBox {
							spacing = 10
							
							val button = new Button("Choose file")
							button.onAction_=({ (_:ActionEvent) =>
								println("Don't you have enough books?")
								try {
								  val fileChooser = new FileChooser()
								  val result = fileChooser.showOpenDialog(stage)
								  if(result != null)
								  {
								    model.file = result
								    model.filePath = model.file.getAbsolutePath()
								    filePath.text = model.filePath
								  }
								  
								}
								catch {
								  case e: NullPointerException => println("WTF, null pointer?")
								}
							})
							
							content = List(
									new Label("Choose file") ,
									button,
									filePath
									)
						},
						new Label {
							text = "Book data"
									font = new Font("Verdana", 16)
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
						} ,
						new Label {
							text = "Author data"
									font = new Font("Verdana", 16)
						},
						useAuthor,
						authorBox,
						authorName,
						new HBox {
							spacing = 10
									content = List(
											new Label {
												text = "Info"
											},
											authorInfo
											)
						},
						useCategory,
						categoryBox,
						categoryName,
						new Button("Add to library") {
							onAction = {e: ActionEvent => 
							println("Oki-doki, adding...")
							val book: Book = createBookBasedOnForm()
							model.db.addBook(book)
							assert(model.db.findBook(book.getTitle) != null,
							    "Just added book cannot be found")
							model.books += book
							model.categories.root.addSubcategory(book.category)
							model.namesOfAllCategories += book.category
							println("Book model: " + model.books)
							}
						}
						)
				def createBookBasedOnForm(): Book = {
				  val title = bookTitle.text.value
				  val path = filePath.text.value
				  val description = bookDescription.text.value
				  val nameOfAuthor = authorName.text.value
				  val additionalInfoAboutAuthor = authorInfo.text.value
				  val category = categoryName.text.value
				  val author = new Author(nameOfAuthor, additionalInfoAboutAuthor)
				  new Book(title, author, path, description, category)
				}
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


