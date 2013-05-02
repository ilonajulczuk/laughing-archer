package hello

import analysis.{CategoryTree}
import swing._
import swing.TabbedPane.Page
import mobireader.Book
import analysis.SummaryTool

object World extends SimpleSwingApplication{
	val db = new DBHandler("my_books.db");
	db.createTablesInDB()
    
    def createLibrary() = {
    	val path = "/example/library/"
    	val author = new Author("John Doe")
    	List(new Book("Dynamics", author, path, "", "Physics"),
    			new Book("Calculus for dummies", author, path, "Easy", "Math"),
    			new Book("Worms", author, path, "Bleh", "Biology"),
    			new Book("Algebra for dummies", author, path, "Easy", "Math"),
    			new Book("Birds", author, path, "Nice", "Biology"),
    			new Book("Black magic", author, path, "Hard", "Math"),
    			new Book("Nothing brings something", author, path, "There isn't really much to say", "Fiction"),
    			new Book("Programming in Scala", author, path, "Very long, but well written", "Computer Science"),
    			new Book("Magnetic fields", author, path, "Very Hard", "Physics")
    			)
    }
    val library = createLibrary()
    assert(library.size > 0, "Library not generated")
    db.addBooks(library)
    
    db.findBookByCategory("Math")
    db.findBooksByAuthor(new Author("John Doe"))
    def blackMagic = db.findBook("Black magic")	
    
    assert(blackMagic.category == "Math", "Is " + blackMagic.category + ", but should be " + "Math")
    
    var myBooks = db.getAllBooks();
    assert(myBooks.size > 0)
    var categories = new CategoryTree
    categories.addSubcategories(List("Science", "Fiction", "Art"))
    categories("Science").addSubcategories(List("Math", "Physics", "Biology", "Computer Science"))
    
    
    def createViewOfBook(book: Book) = {
    	var bookView = List(new Label(book.getTitle))
    	bookView = bookView :+ new Label(book.getAuthor().getName())
    	bookView = bookView :+ new Label(book.getAuthor().getAdditionalInfo())
    	bookView = bookView :+ new Label(book.description)
    	bookView
    }
    
	val booksBox = new BoxPanel(Orientation.Vertical) {
    	
    	val table = new Table(50, 5)
    	for ((book, i) <- myBooks.zipWithIndex)
    	{
    		table.update(i, 0, book.getTitle)
    		table.update(i, 1, book.getAuthor.getName())
    		table.update(i, 2, book.getTitle)
    		table.update(i, 3, book.description)
    		table.update(i, 4, book.category)
    	}
    	table.selectionBackground =new Color(240, 150, 190)
    	val scrollTable = new ScrollPane(table)
    	contents += scrollTable
	}
	
	val addBookBox = new BoxPanel(Orientation.Vertical)
	{
		val buttons = new FlowPanel()
		{
			contents += new Label("Don't you have enough books?")
			contents += new Button("Choose file")
		}
		contents += buttons
	}
	
    val moreInfoAboutBookBox = new BoxPanel(Orientation.Vertical)
    {
    	contents += new Label("More info soon...")
    }
    
    val split = new SplitPane(Orientation.Vertical, booksBox, moreInfoAboutBookBox)
    val categoryBox = new BoxPanel(Orientation.Vertical) {
    	
    	contents += new Label("Categories of books")
    	for(categoryName <- categories.namesOfSubcategories)
    	{
    		contents += new Label("	" + categoryName)
    	}
	}
    
    def top = new MainFrame {
    	minimumSize = new Dimension(600, 200)
    	title = "Mobireader Lib"
    			contents = new BoxPanel(Orientation.Horizontal) {
    			background = new Color(240, 50, 120)
    			val tabs = new TabbedPane()
    			{
    				pages += new Page("Some books", split)
    				pages += new Page("Some categories", categoryBox) 
    				pages += new Page("Add book", addBookBox) 
    			}
    			contents += tabs
    	} 
    	new SummaryTool().main()
    }

}

