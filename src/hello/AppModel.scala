package hello

import mobireader.Mobi
import analysis.CategoryTree
import mobireader.Book
import scalafx.beans.property.DoubleProperty
import scalafx.collections.ObservableBuffer
import java.io.File
import scalafx.Includes._

import scalafx.scene.control._

class AppModel {
	val db = new DBHandler("my_books.db");
	db.createTablesInDB()

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
	
	var mobi: Mobi = null
	
	val library = createLibrary()
	db.addBooks(library)
	var myBooks = db.getAllBooks()
	def getBooks() = {
		val books = new ObservableBuffer[Book]()
		for(book <- db.getAllBooks())
		{
			books += book
		}
		books
	}

	val books = getBooks()

	def updateBooks() {
		for(book <- getBooks())
			if(!(books contains book)) books += book
	}
	val listViewItems = new ObservableBuffer[String]()
	val choiceBoxItems = ObservableBuffer("Choice A", "Choice B", "Choice C", "Choice D")
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
}