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
    			new Book("Black magic", author, path, "Hard", "Math"),
    			new Book("True love", author, path, "Boring", "Fiction"))
    }
    val library = createLibrary()
    assert(library.size > 0, "Library not generated")
    db.addBooks(library)
    
    db.addBook(new Book("True love", new Author("John Doe"),
    		"/example/library/", "Boring", "Fiction"))
    def trueLove = db.findBook("True love")	
    
    assert(trueLove.getAuthor.getName == "John Doe", "True love has wrong author")
    
    var myBooks = db.getAllBooks();
    assert(myBooks.size > 0)
    var categories = new CategoryTree
    categories.addSubcategories(List("Science", "Fiction", "Art"))
    categories("Science").addSubcategories(List("Math", "Physics", "Biology"))
    
	lazy val firstBox = new BoxPanel(Orientation.Vertical) {
    	var titles = 
    	for { book <- myBooks}
    	{
    		contents += new Label(book.getTitle)
    		contents += new Label(book.category)
    		println("adding book: " + book.getTitle)
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

