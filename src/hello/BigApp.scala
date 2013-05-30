package hello

import java.util.prefs.Preferences
import analysis.{Category, CategoryTree, BookAnalyzer, CategoryClassifier}
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
import scalafx.stage.{Stage, Popup, FileChooser, DirectoryChooser, Modality}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import scalafx.collections.ObservableBuffer
import scalafx.scene.text.Font
import scalafx.scene.layout.Region
import javafx.{stage => jfxs}
import javafx.scene.control.Dialogs
import scala.collection.mutable.MutableList
import mobireader.{Mobi, MobiContentParser, MobiDescriptor}
import java.io.File

object BigMain extends JFXApp {

	private val model = new AppModel()
	val treeViewRoot = new TreeItem[String] {
		value = "Root"
		children = createNodeListForTree()
	}
	
	var accordionViewOfAuthors = createAccordionViewOfAuthors()
	
	var tableOfBooks = createTableOfBooks()
	var accordionOfAuthors = createAccordionOfAuthors()
	var treeOfCategories = createTreeOfCategories()
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
	
	val lib = getLibraryPath()
	if(lib != null) {
	  model.libraryPath.value = getLibraryPath()
	  stage.setTitle("Laughing archer with library at - " + lib)
	}
	val work = getWorkspacePath()
	if(work != null)
		model.workspacePath.value = getWorkspacePath()
	
	
	private def createTabs(): TabPane = {
			new TabPane {
				tabs = List(
						new Tab {
							text = "All books"
									content = tableOfBooks
									closable = false
						},
						/*new Tab {
							text = "Authors"
									content = accordionOfAuthors
									closable = false
						},
						new Tab {
							text = "Categories"
									content = treeOfCategories
									closable = false
						},*/
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
									content = createPreferencesView
									closable = false
						}
						)
			}
	}
	
	private def createPreferencesView(): Node = {
	  val libraryPath = new Label {
	    text = model.libraryPath.value
	  }
	  libraryPath.text.bind(model.libraryPath)
	  
	  val changeLibraryPathButton = new Button("Change") {
	    inner =>
		onAction = { e: ActionEvent =>
			val chooser = new DirectoryChooser()
			val result = chooser.showDialog(stage)
			
			if(result != null)
			{
				model.libraryPath.value = result.getAbsolutePath()
				setLibraryPath(result.getAbsolutePath())
			}
		}
	
	  }
	  
	  val workspacePath = new Label {
	    text = model.workspacePath.value
	  }
	  workspacePath.text.bind(model.workspacePath)
	  
	  val changeWorkspacePathButton = new Button("Change") {
	    inner =>
		onAction = { e: ActionEvent =>
			val chooser = new DirectoryChooser()
			val result = chooser.showDialog(stage)
			if(result != null)
			{
				model.workspacePath.value = result.getAbsolutePath()
				setWorkspacePath(result.getAbsolutePath())
			}
		}
	  }
	  
	  val preferences =  new ScrollPane {
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
					libraryPath,
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
	  preferences
	}
	
	private def createMobiParsingView(): Node = {
	  val bookHtml = new TextArea{    
					text = "No book html yet."
					wrapText = true
					editable = false
				}
	  val bookText = new TextArea {
		text = "Content not available"
		wrapText = true
		editable = false
	  }
	  val left = new VBox {
				content = bookHtml 
	  }
	  
	  bookHtml.prefHeight.bind(left.prefHeightProperty)
	  bookHtml.prefWidth.bind(left.prefWidthProperty)
	  bookHtml.prefColumnCount = 35
	  bookHtml.prefRowCount = 35
	  bookText.prefRowCount = 35
			val right = new VBox {
				content = new HBox {
					content = bookText
				}
			}
			
			val header = new VBox {
					spacing = 10
					margin = Insets(10, 10, 10, 10)
					content = List(
					    new Label {
							    	text = "Mobi book preview"
							    	font = new Font("Verdana", 20)
							    },
						new HBox {
						  vgrow = scalafx.scene.layout.Priority.ALWAYS
						  hgrow = scalafx.scene.layout.Priority.ALWAYS
						  spacing = 10
						  val filePath = new Label {
						    text = "No path"
						  }
							val button = new Button("Choose file")
							button.onAction_=({ (_:ActionEvent) =>
								try {
								  val fileChooser = new FileChooser()
								  val result = fileChooser.showOpenDialog(stage)
								  if(result != null && 
								      result.getAbsolutePath().endsWith("mobi") 
								      || result.getAbsolutePath().endsWith("bin"))
								  {
								    filePath.text = result.getAbsolutePath()
								    model.mobi = new Mobi(result.getAbsolutePath())
								    model.mobi.parse()
								    val html = model.mobi.readAllRecords()
								    val parser = new MobiContentParser(html)
								    bookHtml.text = html
								    val textToBeShown = parser.bodyWithParagraphs
								    if (textToBeShown == "")
								      model.bookText = parser.bodyText
								    else
								      model.bookText = textToBeShown
								    bookText.text = model.bookText
								    metadataButton.visible = true
								    analyzeButton.visible = true
								  }
								  else 
								      Dialogs.showWarningDialog(stage, 
								          "File has to be in mobi format", "Reading failure")
								}
								catch {
								  case e: NullPointerException => println("WTF, null pointer?")
								}
							})
							
							val metadataButton = new Button("Show metadata")
							metadataButton.onAction_=({ (_:ActionEvent) =>
							  if(model.mobi != null) 
							  {
							    val descriptor = new MobiDescriptor(model.mobi)
							    
							    def addIndentation(paragraph: String) = {
							     (for (sentence <- paragraph.split("\n"))
							       yield "  " + sentence).mkString("\n") + "\n"
							    }
							    
							    var description ="First header:\n" + addIndentation(descriptor.firstHeaderInfo)
							    description += "\nPalmdoc header:\n" + addIndentation(descriptor.palmdocHeaderInfo)
							    description += "\nMobi header:\n" + addIndentation(descriptor.mobiHeaderInfo)
							    Dialogs.showInformationDialog(stage, 
								          description, "Mobi file contains multiple headers, " +
							    "the most important have following data:", "Mobi metadata")
							  }
							  
							})
							
							metadataButton.visible = false
							
							val analyzeButton = new Button("Analyze content")
							analyzeButton.onAction_=({ (_:ActionEvent) =>
							  showBookAnalyzis()
							})
							
							metadataButton.visible = false
							analyzeButton.visible = false
							content = List(
							    filePath,
							    button,
							    metadataButton,
							    analyzeButton
							)
						}
							    )
			}
			val body = new SplitPane {
							 items.addAll(
							    left,
							    right
							 )
			}
			
			val split = new SplitPane() {
			  orientation =  Orientation.VERTICAL
				items.addAll(
				    header,
				    body
				)
			}
			split.setDividerPositions(0, 0.25)
			split
	}
	
	def createAnalyzisPage(): Node = {
	  val analyzer = new BookAnalyzer
	  val summaryText = analyzer.makeSummary(model.shortenBookText)
	  
	  val summary = new TextArea {
		text = summaryText
		wrapText = true
		editable = false
	  }
	  
	  val analyzis = new VBox {
	    padding = Insets(10)
	    spacing = 10
		margin = Insets(10, 10, 10, 10)
	    content = List( 
	        new Label("Results of analyzis") {
	    		font = new Font("Verdana", 20)
	    	},
	    	new Label("Summary") {
	    		font = new Font("Verdana", 14)
	    	},
	        summary,
	        new Label("Most common words"){
	    		font = new Font("Verdana", 14)
	    	},
	    	new Label("Asossiated categories"){
	    		font = new Font("Verdana", 14)
	    	},
	    	new Label() {
	    	  var maxTextSize = model.bookText.size
	    	  if(maxTextSize  > 1000) {
	    	    maxTextSize = 1000
	    	  }
	    	  val winningCategory = CategoryClassifier.matchCategory(model.bookText.slice(0, maxTextSize))
	    	  text = winningCategory._1 + " with " + winningCategory._2 +  "% match" 
	    	}
	      )
	    }
	  
	  summary.prefHeight.bind(analyzis.prefHeightProperty)
	  summary.prefWidth.bind(analyzis.prefWidthProperty)
	  summary.prefColumnCount = 35
	  summary.prefRowCount = 35
	  summary.prefRowCount = 35
	  
	  new ScrollPane {
		margin = Insets(10, 10, 10, 10)
	    prefWidth = 500
	    prefHeight = 580
	    content = analyzis
	  }
	}
	
	def showBookAnalyzis() {
			val page = createAnalyzisPage();
			val dialogStage = new Stage();
			dialogStage.setTitle("Book Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage)
			val scene = new Scene()
			scene.content = page
			dialogStage.setScene(scene)
			dialogStage.show()
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

			val table = new TableView[Book](model.books) {
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

	private def createAccordionOfAuthors(): Accordion = new Accordion {
		panes = accordionViewOfAuthors
		expandedPane = panes.head
	}

	def createAccordionViewOfAuthors() : MutableList[TitledPane] = {
		val authors = model.authors
		val mutableAuthors = new MutableList[TitledPane]
		for(author <- authors) {
		  mutableAuthors += new TitledPane {
		    text = author.getName
			content = new TextArea {
				text = author.provideAllInfo()
			}
		  }
		}
		mutableAuthors
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
				root = treeViewRoot
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
			var usingAlreadyAddedAuthor = false
			var usingAlreadyAddedCategory = false
			val addingBox = new VBox {
				padding = Insets(20)
						vgrow = scalafx.scene.layout.Priority.ALWAYS
						hgrow = scalafx.scene.layout.Priority.ALWAYS
						spacing = 10
						margin = Insets(50, 0, 0, 50)
						var chosenFileName = ""

						val bookTitle = new TextField {
						promptText = "Title"
						text = ""
						prefColumnCount = 16
							
				}

				val bookDescription = new TextArea {
					prefColumnCount = 20
					prefRowCount = 2
							
				}

				val useAuthor = new CheckBox("Use already added author") {
					inner =>
					onAction = { e: ActionEvent =>
						println(e.eventType + " occured on CheckBox, and `selected` property is: " + inner.selected())
						if(inner.selected()) {
						  usingAlreadyAddedAuthor = true
						authorName.text.value = authorBox.value.value
						authorInfo.text.value = model.db.findAuthor(authorBox.value.value).additionalInfo
						authorName.editable = false
						authorInfo.editable = false
						}
						else {
						  usingAlreadyAddedAuthor = false
						authorName.text.value = authorBox.value.value
						authorInfo.text.value = model.db.findAuthor(authorBox.value.value).additionalInfo
						authorName.editable = true
						authorInfo.editable = true
						}
						
					}
				}
				
				val filePath = new Label("Nothing selected")
				filePath.wrapText = true
				
				val authorBox = new ChoiceBox(ObservableBuffer(for (author <- model.db.getAllAuthors) yield author.getName)) {
					selectionModel().selectFirst()
					selectionModel().selectedItem.onChange(
					    {
							(_, _, newValue) => println(newValue + " chosen in ChoiceBox")
							if(usingAlreadyAddedAuthor)
							{
							  authorName.text.value_=(newValue)
							}
					    }
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
					usingAlreadyAddedCategory = inner.selected()
					if(usingAlreadyAddedCategory) {
					  categoryName.text.value = categoryBox.value.value
					  categoryName.editable = false
					}
					else
					  categoryName.editable = true
					}
				}

				val categoryBox = new ChoiceBox[String] {
					maxWidth = 80
					minWidth = 80
					maxHeight = 50
					items = model.namesOfAllCategories
					selectionModel().selectFirst()
					selectionModel().selectedItem.onChange(
					    {
					      (_, _, newValue) => println(newValue + " chosen in ChoiceBox")
					      if(usingAlreadyAddedCategory)
							{
							  categoryName.text.value_=(newValue)
							}
					    }
					)
				}

				val categoryName = new TextField {
					promptText = "Category"
							prefColumnCount = 16
				}
				
				content = List(
						new VBox {
							spacing = 10
							val button = new Button("Choose file")
							button.onAction_=({ (_:ActionEvent) =>
								try {
								  val fileChooser = new FileChooser()
								  val result = fileChooser.showOpenDialog(stage)
								  if(result != null)
								  {
								    val path = result.getAbsolutePath()
								    if (model.isBookFormatSupported(path)) {
								      if (bookTitle.text == "" || bookTitle.text == null || bookTitle.text == "Title")
								      {
								        println("Updating title")
								    	  bookTitle.text = model.getBookTitleFromPath(path)
								      }
								      else {
								        println("No need for updating title")
								      }
								      filePath.text = path
								      model.updateBookFormatFromPath(path)
								      model.updateBookText(path)
								      println("Upadting preview")
								      model.updatePreviewOfBookText()
								      model.file = result
								      model.filePath = path
								    }
								    else {
								      Dialogs.showWarningDialog(stage,  "Book format: " +
								          model.getBookFormatFromPath(path) + " is not supported",
								    		  "Book couldn't be added.", "Adding failure")
								    }
								  }
								  
								}
								catch {
								  case e: NullPointerException => println("WTF, null pointer?")
								}
							})
							content = List(filePath, button)
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
						useAuthor,
						authorBox,
						authorName,
						new HBox {
						  spacing = 10
						  content = List(
							authorInfo
						  )
						},
						useCategory,
						categoryBox,
						categoryName,
						new Button("Add to library") {
							onAction = {e: ActionEvent => 
							val emptyFields = getEmptyFields() 
			
							if(!emptyFields.isEmpty) {
							  val capitalizedEmptyFields = emptyFields(0)(0).toUpper +
								emptyFields(0).tail :: emptyFields.tail
							  Dialogs.showWarningDialog(stage, capitalizedEmptyFields.mkString(", ") + " cannot be empty.",
							      "Book couldn't be added.", "Adding failure")
							
							}
							else {
								val book: Book = createBookBasedOnForm()
								if(!model.books.contains(book)) { //TODO make it working
									model.db.addBook(book)
									assert(model.db.findBook(book.getTitle) != null,
								    "Just added book cannot be found")
									model.books += book
									model.namesOfAllCategories += book.category
									model.categories.addSubcategories(List(book.category))
									val author = book.getAuthor
									author.addTitle(book.getTitle)
									treeViewRoot.children = createNodeListForTree()
									accordionViewOfAuthors = createAccordionViewOfAuthors()
									accordionOfAuthors.panes = accordionViewOfAuthors
							    }
							}
						}
					}
				)
				def getEmptyFields(): List[String] = {
				  var emptyFields = List[String]()
				  if(bookTitle.text.value == "")
				    emptyFields = "title" :: emptyFields
				  if(filePath.text.value == "")
				    emptyFields = "path to file" :: emptyFields
				  if(bookDescription.text.value == "")
				    emptyFields = "book description" :: emptyFields
				  if(authorName.text.value == "")
				    emptyFields = "name of the author" :: emptyFields
				  if(authorInfo.text.value == "")
				    emptyFields = "info about the author" :: emptyFields
				  if(categoryName.text.value == "")
				    emptyFields = "category" :: emptyFields
				  emptyFields
				}		
				
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
					previewText.prefColumnCount = 30
					previewText.prefRowCount = 30
					previewText.text.bind(model.bookTextPreview)
					previewTitle.text.bind(addingBox.bookTitle.text)
					content = List(
						previewTitle,
						previewText
					)
				}
			}
			
			new SplitPane {
						items.addAll(
								left,
								right
						)
			}
	}
	
	def getLibraryPath(): String = {
		val prefs = Preferences.userNodeForPackage(classOf[AppModel])
		prefs.get("libraryPath", null)
	}
	
	def getWorkspacePath(): String = {
		val prefs = Preferences.userNodeForPackage(classOf[AppModel])
		prefs.get("workspacePath", null)
	}
	
	def setLibraryPath(path: String) {
		val prefs = Preferences.userNodeForPackage(classOf[AppModel])
		if (path != null) {
			prefs.put("libraryPath", path)
			stage.setTitle("Laughing archer with library at - " + path)
		} else {
			prefs.remove("libraryPath")
			stage.setTitle("Laughing archer")
		}
	}
	
	def setWorkspacePath(path: String) {
		val prefs = Preferences.userNodeForPackage(classOf[AppModel])
		if (path != null) {
			prefs.put("workspacePath", path)
		} else {
			prefs.remove("workspacePath")
		}
	}
}


