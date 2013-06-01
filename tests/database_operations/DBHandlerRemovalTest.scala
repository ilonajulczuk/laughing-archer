package database_operations

import hello.DBHandler
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import mobireader.Book
import hello.Author

class DBHandlerRemovalTest extends FunSuite with BeforeAndAfter {
  var db: DBHandler = _
  val author1 = new Author("Bob", "likes trains")
  val author2 = new Author("Alice")
  val book1 = new Book("Book1", author1 )
  val book2 = new Book("Book2", author1 )
  val book3 = new Book("Book3", author1 )
  val book4 = new Book("Book4", author2 )

  before {
    db  = new DBHandler("test.db")
    db.createTablesInDB()
    db.addBooks(List(book1, book2, book3, book4))
  }

  test("Remove one book [simple case]") {
    db.removeBook(book1)
    assert(db.findBook(book1.getTitle) === None)
  }

  test("Remove two books [one author should also be removed]") {
    db.removeBook(book1)
    db.removeBook(book4)
    assert(db.findAuthor(author2.getName).getName === "Unknown")
  }

  test("Remove one author who written one book") {
    db.removeAuthor(author2)
    assert(db.findAuthor(author2.getName).getName === "Unknown")
    assert(db.findBook(book4.getTitle) === None)
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
