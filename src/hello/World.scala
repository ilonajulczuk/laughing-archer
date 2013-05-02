package hello

import analysis.{CategoryTree}
import swing._
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
    			new Book("Black magic", author, path, "Hard", "Math")
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
    categories("Science").addSubcategories(List("Math", "Physics", "Biology"))
    
    
    def createViewOfBook(book: Book) = {
    	var bookView = List(new Label(book.getTitle))
    	bookView = bookView :+ new Label(book.getAuthor().getName())
    	bookView = bookView :+ new Label(book.getAuthor().getAdditionalInfo())
    	bookView = bookView :+ new Label(book.description)
    	bookView
    }
    
	lazy val firstBox = new BoxPanel(Orientation.Vertical) {
    	for (categoryName <- categories.namesOfSubcategories)
    	{
    		contents += new Label("Main category")
    		contents += new Label(categoryName)
    		for(category <- categories(categoryName).namesOfSubcategories)
    		{
    			contents += new Label(category)
    			val books = db.findBookByCategory(category)
    			println("In " + category + " is/are " + books.size + " books")
    			for(book <- books)
    			{
    				val viewList = createViewOfBook(book)
    				assert(!viewList.isEmpty, "View list is empty")
    				for(item <- viewList)
    					contents += item
    				contents
    			}
    		}
    		contents
    	}
	}
    
    lazy val secondBox = new BoxPanel(Orientation.Vertical) {
    	
    	contents += new Label("Categories of books")
    	for(categoryName <- categories.namesOfSubcategories)
    	{
    		contents += new Label("	" + categoryName)
    	}
	}
    
    def top = new MainFrame {
    	minimumSize = new Dimension(400, 200)
    	title = "Mobireader Lib"
    			contents = new BoxPanel(Orientation.Horizontal) {
    			
    			contents += firstBox
    			contents += secondBox 
    	} 
    	new SummaryTool().main()
    }

}

