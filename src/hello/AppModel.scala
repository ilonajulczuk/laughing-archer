package hello

import mobireader.{Mobi, MobiContentParser}
import odt.OpenOfficeParser
import analysis.CategoryTree
import mobireader.Book
import scalafx.beans.property.DoubleProperty
import scalafx.collections.ObservableBuffer
import java.io.{File, RandomAccessFile}
import scalafx.Includes._
import scalafx.scene.control._
import javafx.beans.property.SimpleStringProperty
import sun.security.util.Length
class AppModel {
	val db = new DBHandler("my_books.db");
	db.createTablesInDB()

	def addDummyLibrary() {
	  def createLibrary() = {
		val path = "/example/library/"
				val author1 = new Author("John Doe")
		val author2 = new Author("George Smith")
		val author3 = new Author("Anna White")
		List(new Book("Dynamics", author1, path, "", "Physics"),
				new Book("Calculus for dummies", author2, path, "Easy", "Math"),
				new Book("Worms", author3, path, "Bleh", "Biology"),
				new Book("Algebra for dummies", author1, path, "Easy", "Math"),
				new Book("Birds", author1, path, "Nice", "Biology"),
				new Book("Black magic", author2, path, "Hard", "Math"),
				new Book("Nothing brings something", author1, path, "There isn't really much to say", "Fiction"),
				new Book("Programming in Scala", author3, path, "Very long, but well written", "Computer Science"),
				new Book("Magnetic fields", author2, path, "Very Hard", "Physics")
			)
	  }
	  db.addBooks(createLibrary)
	}
	
	def normalizePath(path: String, maxSize: Int) = {
	  val parts = path.split("/")
	  if (path.size > maxSize) {
		  parts.head + "/.../" + parts(parts.size -2) + "/" +parts.last
	  }
	  else 
	    path
	}
	
	var libraryPath = new SimpleStringProperty("./library/")
	var workspacePath = new SimpleStringProperty("./workspace/")
	
	val bookInfoUtility = new BookInfoUtility
	var mobi: Mobi = null
	
	
	var myBooks = db.getAllBooks()
	
	def isBookFormatSupported(path: String) = {
	  val format = bookInfoUtility.extractFormatFromPath(path)
	  bookInfoUtility.isFormatSupported(format)
	}
	
	def getBookTitleFromPath(path: String) = {
	  bookInfoUtility.extractTitleFromPath(path)
	}
	
	var bookFormat = ""
	def updateBookFormatFromPath(path: String) {
	  bookFormat = getBookFormatFromPath(path)
	}
	
	def getBookFormatFromPath(path: String) = {
	  bookInfoUtility.extractFormatFromPath(path)
	}
	val books = getBooks()

	def getBooks() = {
		val books = new ObservableBuffer[Book]()
		for(book <- db.getAllBooks())
		{
			books += book
		}
		books
	}
	
	def updateBooks() {
		for(book <- getBooks())
			if(!(books contains book)) books += book
	}
	
	
	val listViewItems = new ObservableBuffer[String]()
	var categories = new CategoryTree
	categories.addSubcategories(List("Science", "Fiction", "Art"))
	categories("Science").addSubcategories(List("Math", "Physics", "Biology", "Computer Science"))
	categories("Fiction").addSubcategories(List("Science Fiction", "Soap operas", "Horror", "Fantasy"))
	categories("Fiction")("Science Fiction").addSubcategories(List("Hard", "Ambitious", "Voyage"))

	var namesOfAllCategories: ObservableBuffer[String] = ObservableBuffer(for (category <-
					categories.allNames) yield category)
					
	def authors = db.getAllAuthors
	var filePath: String = ""
	var file: File = _
	
	var bookText = ""
	  
	val bookTextPreview = new SimpleStringProperty("No preview available")
	
	def shortenBookText = {
	  val paragraphs = bookText.split("\n\n")
	  val chosenParagraphs = paragraphs.slice(0, 20) ++ paragraphs.slice(paragraphs.size - 50, paragraphs.size -20)
	  println (paragraphs.size)
	  println(chosenParagraphs.mkString("\n\n"))
	  chosenParagraphs.mkString("\n\n")
	}
	
	def updateBookText(path: String) {
	  if (bookFormat == "mobi" || bookFormat == "bin") {
	    val mobiParser = new Mobi(path)
	    mobiParser.parse()
	    val mobiContentParser = new MobiContentParser(mobiParser.readAllRecords())
	    bookText = mobiContentParser.bodyWithParagraphs
	  }
	  else if(bookFormat == "odt") {
	    val odtParser = new OpenOfficeParser()
	     bookText = odtParser.getText(path)
	  }
	  else if(bookFormat == "txt") {
	    val book = new File(path)
	    val fp = new RandomAccessFile(book, "r")
	    val len = book.length.toInt
		val buff = new  Array[Byte](book.length.toInt)
		fp.readFully(buff)
		bookText = new String(buff)
	  }
	}
	
	def updatePreviewOfBookText() {
	  val shortened = bookText.substring(0, (bookText.size*0.05).toInt)
	  val lastSpace = shortened.lastIndexOf(' ')
	  if (lastSpace != -1)
		  bookTextPreview.value = if (shortened(lastSpace -1) == '.') {
			  shortened.substring(0, lastSpace) + "..\n"
		  }
		  else shortened.substring(0, lastSpace) + "...\n"
	}
}


class BookInfoUtility {
	val supportedBookFormats = List("mobi", "bin", "odt", "txt")
	val fileSeparator = "/"
	  
	def extractLastWordFromPath(path: String) = {
	  path.split(fileSeparator).last
	}
	
	def extractFormatFromPath(path: String) = {
	  extractLastWordFromPath(path).split("\\.").last
	}
	
	def extractTitleFromPath(path: String) = {
	  extractLastWordFromPath(path).split("\\.").head
	}
	
	def isFormatSupported(format: String) = {
	  supportedBookFormats contains format
	}
}
	