package hello

import analysis.{CategoryTree}
import swing._
import mobireader.Book
import analysis.SummaryTool

object World extends SimpleSwingApplication{
	

	val db = new DBHandler("my_books.db");
	db.createTablesInDB();
	db.addBooks(Book.createSomeExamples );
    var myBooks = db.getAllBooks();
    
    var categories = new CategoryTree
    categories.addSubcategories(List("Science", "Fiction", "Art"))
    categories("Science").addSubcategories(List("Math", "Physics", "Biology"))
    
	lazy val firstBox = new BoxPanel(Orientation.Vertical) {
    	var titles = new Array[String](myBooks.size)
    	for ( i <- 0 to myBooks.size -1) {
    		titles(i) = myBooks.get(i).getTitle()
    	}
    	contents += new ComboBox(titles)
    	
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

