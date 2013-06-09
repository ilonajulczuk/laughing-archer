package database_operations

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import domain.{Category, Book, Author}
import db.{DBHandler, BookNotFound}

class DBHandlerRemovalTest extends FunSuite with BeforeAndAfter {
  var db: DBHandler = _
  val defaultCategory = new Category("Fiction")
  val author1 = new Author("Bob", "likes trains")
  val author2 = new Author("Alice")
  //TODO refactor it, DRY
  val book1 = new Book("Book1", author1)
  book1.category = defaultCategory
  val book2 = new Book("Book2", author1 )
  book2.category = defaultCategory
  val book3 = new Book("Book3", author1 )
  book3.category = defaultCategory
  val book4 = new Book("Book4", author2 )
  book4.category = defaultCategory

  before {
    db  = new DBHandler("test.db")
    db.createTablesInDB()
    db.addBooks(List(book1, book2, book3, book4))
  }

  test("Remove one book [simple case]") {
    db.removeBook(book1)
    intercept[BookNotFound]{db.findBook(book1.getTitle)}
  }

  test("Remove two books [one author should also be removed]") {
    db.removeBook(book1)
    db.removeBook(book4)
    assert(db.findAuthor(author2.getName).getName === "Unknown")
  }

  test("Remove one author who written one book") {
    println(author2.id)
    db.removeAuthor(author2)
    assert(db.findAuthor(author2.getName).getName === "Unknown")
    intercept[BookNotFound]{db.findBook(book4.getTitle)}
  }

  test("Remove author with multiple books") {
    db.removeAuthor(author1)
    assert(db.findAuthor(author1.getName).getName === "Unknown")
    assert(db.findBooksByAuthor(author1) === List[Book]())
  }

  after {
    db.dropAllTables()
  }
}
