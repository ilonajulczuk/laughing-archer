package hello


import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.ArrayList

import domain.{Book, Author}

class StatementBuilder {
  private def prepareStatement(connection: Connection,
                               statementText: String) = {
    val statement = connection.prepareStatement(
      statementText)
    statement.setQueryTimeout(30) // set timeout to 30 sec.
    statement
  }

  def findAuthorByNameStatement(connection: Connection) =
    prepareStatement(connection, "select * from authors where name=?")

  def addAuthorStatement(connection: Connection) =
    prepareStatement(connection, "insert into " +
      "authors(name, additional_info) values( ?, ?)")

  def removeAuthorStatement(connection: Connection) =
    prepareStatement(connection, "delete from authors where name =" +
      "  ?")

  //TODO checkout, if replacing really works as supposed to
  def addBookStatement(connection: Connection) =
    prepareStatement(connection, "insert into books(title, author_name," +
      " path_to_content, description, category) values( ?, ?, ?, ?, ?)")

  def removeBookStatement(connection: Connection) =
    prepareStatement(connection, "delete from books where title = ? and author_name = ?")

  def findBookStatement(connection: Connection) =
    prepareStatement(connection, "select b.title, b.author_name," +
      " b.path_to_content, b.description, b.category,"
      + " a.additional_info from books b left join authors a on"
      + " b.author_name = a.name where b.title=?")

  def findBookByCategoryStatement(connection: Connection) =
    prepareStatement(connection, "select b.title, b.author_name, b.path_to_content," +
      " b.description, b.category,"
      + " a.additional_info from books b left join authors a on"
      + " b.author_name = a.name where b.category=?")

  def findBookByAuthorStatement(connection: Connection) =
    prepareStatement(connection, "select * from books where author_name=?")

  def getAllBooksStatement(connection: Connection) =
    prepareStatement(connection, "select b.title, b.author_name, b.path_to_content," +
      " b.category, b.description,"
      + " a.additional_info from books b left join authors a on"
      + " b.author_name = a.name")

  def getAllAuthorsStatement(connection: Connection) =
    prepareStatement(connection, "select * from authors")
}

class DBHandler(dbFile: String) {

  val driver = "org.sqlite.JDBC"
  val statBld = new StatementBuilder()

  def this() = {
    this("sample.db")
  }

  def prepareConnection() = {
    Class.forName(this.driver)
    DriverManager.getConnection("jdbc:sqlite:" + dbFile)
  }

  def addBooks(books: List[Book]) {
    books.foreach(addBook)
  }

  def addBook(book: Book) {
    val connection = prepareConnection()
    val author = findAuthor(book.getAuthor.getName)
    if (author.name == "Unknown") {
      this.addAuthor(book.getAuthor)
    }
    val stat = statBld.addBookStatement(connection)
    stat.setString(1, book.getTitle)
    stat.setString(2, book.getAuthor.getName)
    stat.setString(3, book.getPathToContent)
    stat.setString(4, book.description)
    stat.setString(5, book.category)
    stat.executeUpdate()

    connection.close()
  }

  def removeBook(book: Book) {
    val connection = prepareConnection()
    val author = findAuthor(book.getAuthor.getName)
    val stat = statBld.removeBookStatement(connection)
    stat.setString(1, book.getTitle)
    stat.setString(2, book.getAuthor.getName)
    stat.executeUpdate()

    if(author.getName != "Unknown") {
      val anotherBooksWrittenByTheSameAuthor = findBooksByAuthor(author)
      if (anotherBooksWrittenByTheSameAuthor.isEmpty)
        removeAuthor(author)
    }
    connection.close()
  }

  def removeAuthor(author: Author) {
    val connection = prepareConnection()
    val books = findBooksByAuthor(author)
    val stat = statBld.removeAuthorStatement(connection)
    stat.setString(1, author.getName)
    stat.executeUpdate()
    for (book <- books) {
      removeBook(book)
    }
    connection.close()
  }

  def makeBookFromResultSet(rs: ResultSet): Book = {
    val path = rs.getString("path_to_content")
    val title = rs.getString("title")
    val category = rs.getString("category")
    val description = rs.getString("description")
    val author_name = rs.getString("author_name")
    val info_about_author = rs.getString("additional_info")
    val author = new Author(author_name, info_about_author)
    new Book(title, author, path, description, category)

  }

  def makeAuthorFromResultSet(rs: ResultSet): Author = {
    val name = rs.getString("name")
    val additionalInfo = rs.getString("additional_info")
    new Author(name, additionalInfo)
  }

  def findBook(title: String) = {
    val connection = prepareConnection()
    val stat = statBld.findBookStatement(connection)
    stat.setString(1, title)
    val rs = stat.executeQuery()
    if(rs.next()) {
      val book = makeBookFromResultSet(rs)
      rs.close()
      connection.close()
      book
    }
    else
      None
  }

  def findBooksByAuthor(author: Author) = {
    val connection = prepareConnection()
    val stat = statBld.findBookByAuthorStatement(connection)
    stat.setString(1, author.getName)
    val rs = stat.executeQuery()
    var books = List[Book]()
    while (rs.next()) {
      val path = rs.getString("path_to_content")
      val title = rs.getString("title")
      books = books :+ (new Book(title, author, path))
    }
    rs.close()
    connection.close()
    books
  }

  def findBooksByCategory(category: String) = {
    val connection = prepareConnection()
    val stat = statBld.findBookByCategoryStatement(connection)
    stat.setString(1, category)
    val rs = stat.executeQuery()
    var books = List[Book]()
    while (rs.next()) {
      books = books :+ makeBookFromResultSet(rs)
    }
    rs.close()
    connection.close()
    books
  }

  def getAllBooks() = {
    val connection = prepareConnection()
    val stat = statBld.getAllBooksStatement(connection)
    val rs = stat.executeQuery()
    var books = List[Book]()
    while (rs.next()) {
      books = books :+ (makeBookFromResultSet(rs))
    }
    assert(books.size > 0, "No books found in db")
    rs.close()
    connection.close()
    books
  }

  import scala.collection.JavaConversions._

  def getAllAuthors() = {
    val connection = prepareConnection()
    val stat = statBld.getAllAuthorsStatement(connection)
    val rs = stat.executeQuery()
    var authors = List[Author]()
    while (rs.next()) {
      val author = (makeAuthorFromResultSet(rs))
      val titles = for (book <- findBooksByAuthor(author))
      yield book.getTitle
      author.addTitles(new ArrayList(titles))
      authors = authors :+ author
    }
    assert(!authors.isEmpty, "No books found in db")
    rs.close()
    connection.close()
    authors
  }

  def addAuthor(author: Author) {
    val connection = prepareConnection()
    val stat = statBld.addAuthorStatement(connection)
    stat.setString(1, author.getName)
    stat.setString(2, author.getAdditionalInfo)
    stat.executeUpdate()

    connection.close()
  }

  def findAuthor(name: String) = {
    try {
      val connection = prepareConnection()
      val stat = statBld.findAuthorByNameStatement(connection)
      stat.setString(1, name)
      stat.executeQuery()
      val rs = stat.executeQuery()
      val author = if (rs.next()) {
        val additional_info = rs.getString("additional_info")
        new Author(name, additional_info)
      }
      else {
        new Author("Unknown")
      }
      rs.close()
      connection.close()
      author
    }
    catch {
      case ex: SQLException => {
        println("SQLException: " + ex.getSQLState)
        new Author("Unknown")
      }
    }
  }

  def createTablesInDB() {
    val connection = prepareConnection()
    val stat = connection.createStatement()
    stat.executeUpdate("create table if not exists books (title string, " +
      "author_name string, path_to_content string," +
      "description string, category string)")
    stat.executeUpdate("create table if not exists authors (name string unique, " +
      "additional_info string)")
    connection.close()
  }

  def dropAllTables() {
    val connection = prepareConnection()
    val stat = connection.createStatement()
    stat.executeUpdate("drop table books")
    stat.executeUpdate("drop table authors")
  }
}
