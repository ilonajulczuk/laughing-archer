package hello

import swing._
import mobireader.Book
import analysis.SummaryTool
object World extends SimpleSwingApplication{
	

	val db = new SqliteDatabaseHandler("my_books.db");
	db.createTablesInDB();
	db.addBooks(Book.createSomeExamples());
    var myBooks = db.getAllBooks();
    
    
    import ComboBox._
		lazy val ui = new FlowPanel {
    		var titles = new Array[String](myBooks.size)
    		for ( i <- 0 to myBooks.size -1) {
    			titles(i) = myBooks.get(i).getTitle()
    		}
		contents += new ComboBox(titles)
	}
    
    def top = new MainFrame {
    	title = "ComboBoxes Demo"
    			contents = ui
    	new SummaryTool().main()
    }

}

