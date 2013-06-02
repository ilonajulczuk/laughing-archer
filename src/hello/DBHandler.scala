package hello


import java.sql.Connection
import java.sql.Statement
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import scala.collection.JavaConversions._

import domain.{Book, Author, Tag, Category}

class StatementBuilder {
  private def prepareStatement(connection: Connection,
                               statementText: String) = {
    val statement = connection.prepareStatement(
      statementText, Statement.RETURN_GENERATED_KEYS)
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
    prepareStatement(connection, "insert into books(title," +
      " path_to_content, description, category_id) values( ?, ?, ?, ?)")

  def addBookAuthorRelation(connection: Connection) =
    prepareStatement(connection, "insert into book_authors(book_id, author_id)" +
      " values( ?, ?)")

  def addBookTagRelation(connection: Connection) =
    prepareStatement(connection, "insert into book_tags(book_id, tag_id)" +
      " values( ?, ?)")

  def addTagStatement(connection: Connection) =
    prepareStatement(connection,"insert into tags(tag) values(?)")

  def addCategoryStatement(connection: Connection) =
    prepareStatement(connection,"insert into categories(category) values(?)")

  def removeBookStatement(connection: Connection) =
    prepareStatement(connection, "delete from books where id = ?")

  def removeBookAuthorRelationStatement(connection: Connection) =
    prepareStatement(connection, "delete from book_authors where book_id = ? and author_id = ?")

  def findBookStatement(connection: Connection) =
    prepareStatement(connection, "select b.title, a.name," +
      " b.path_to_content, b.description, b.category_id,"
      + " a.additional_info from books b left join book_authors r on"
      + " r.author_id = b.author_id left join authors a on r.author_id = a.id where b.title=?")

  def findBookIDByTitleAndAuthorStatement(connection: Connection) =
    prepareStatement(connection, "select b.id"
      + " from books b left join book_authors r on"
      + " r.book_id = b.id left join authors a on r.author_id = a.id where b.title=? and a.name=?")

  def findCategoryByIDStatement(connection: Connection) =
    prepareStatement(connection, "select category from categories where id = ?")

  def findCategoryStatement(connection: Connection) =
    prepareStatement(connection, "select id from categories where category = ?")

  def findBookByCategoryStatement(connection: Connection) =
    prepareStatement(connection, "select b.title, a.name, b.path_to_content," +
      " b.description, b.category,"
      + " a.additional_info from books b left join authors a on"
      + " b.author_name = a.name where b.category=?")

  def findBookByAuthorStatement(connection: Connection) =
    prepareStatement(connection, "select * from books where author_id=?")

  def getAllBooksStatement(connection: Connection) =
    prepareStatement(connection, "select b.title, b.path_to_content," +
      " b.category_id, b.description,"
      + " a.name, a.additional_info from books b left join book_authors r on"
      + " r.book_id = b.id left join authors a on a.id = r.author_id")

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


  def addTag(tag: Tag) {
    val connection = prepareConnection()
    val stat = statBld.addTagStatement(connection)
    stat.setString(1, tag.tag)
    stat.executeUpdate()

    val rs = stat.getGeneratedKeys()
    if (rs.next()){
      tag.id =rs.getInt(1)
    }
    connection.close()
  }

  def findCategoryID(category: Category) = {
    val connection = prepareConnection()
    val stat = statBld.findCategoryStatement(connection)
    stat.setString(1, category.category)
    val rs: ResultSet = stat.executeQuery()
    var id = -1
    if (rs.next()) {
      id = rs.getInt("id")
    }
    connection.close()
    id
  }

  def addCategory(category: Category) {
    val id = findCategoryID(category)
    if(id == -1) {
      val connection = prepareConnection()
      val stat = statBld.addCategoryStatement(connection)
      stat.setString(1, category.category)
      stat.executeUpdate()

      val rs = stat.getGeneratedKeys()
      if (rs.next()){
        category.id =rs.getInt(1)
      }
      connection.close()
    }
    else {
      category.id = id
    }

  }

  def addBookAuthorRelation(book: Book, author: Author)  {
    val connection = prepareConnection()
    val stat = statBld.addBookAuthorRelation(connection)
    stat.setInt(1, book.id)
    stat.setInt(2, author.id)
    stat.executeUpdate()
    connection.close()
  }

  def addBookTagRelation(book: Book, tag: Tag)  {
    val connection = prepareConnection()
    val stat = statBld.addBookTagRelation(connection)
    stat.setInt(1, book.id)
    stat.setInt(2, tag.id)
    stat.executeUpdate()
    connection.close()
  }

  def addBooks(books: List[Book]) {
    for(book <- books)
      addBook(book)
  }

  def addBook(book: Book) {
    addBook(book, List(book.getAuthor))
  }

  def addBook(book: Book, authors: List[Author]) {
    val bookIDFromDB = findBookByTitleAndAuthor(book.getTitle(), authors.head.getName)
    if (bookIDFromDB == -1) {
      println("Already in db")
      val connection = prepareConnection()
      for (author <- authors) {
        if (author.id == -1) {
          addAuthor(author)
        }
      }

      if (book.category.id == -1) {
        addCategory(book.category)
      }

      for (tag: Tag <- book.tags.toList) {
        if (tag.id == -1)
          addTag(tag)
      }

      val stat = statBld.addBookStatement(connection)
      stat.setString(1, book.getTitle)
      stat.setString(2, book.getPathToContent)
      stat.setString(3, book.description)
      stat.setInt(4, book.category.id)
      stat.executeUpdate()

      val rs = stat.getGeneratedKeys()
      if (rs.next()){
        book.id =rs.getInt(1)
      }

      for(author <- authors) {
        addBookAuthorRelation(book, author)
      }

      for(tag: Tag <- book.tags) {
        addBookTagRelation(book, tag)
      }
      connection.close()
    }

  }

  def removeBook(book: Book) {
    val connection = prepareConnection()
    val stat = statBld.removeBookStatement(connection)
    stat.setInt(1, book.id)
    stat.executeUpdate()
    val author = book.getAuthor
    removeBookAuthorRelation(book, author)
    connection.close()
  }

  def isAuthorWithoutBooks(author: Author) = {
    false
  }

  def removeBookAuthorRelation(book: Book, author: Author) {
    val connection = prepareConnection()
    val stat = statBld.removeBookAuthorRelationStatement(connection)
    stat.setInt(1, book.id)
    stat.setInt(2, author.id)
    stat.executeUpdate()
    if (isAuthorWithoutBooks(author))
      removeAuthor(author)
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

  def getCategoryByID(categoryID: Int) = {

    val connection = prepareConnection()
    val stat = statBld.findCategoryByIDStatement(connection)
    stat.setInt(1, categoryID)
    val rs = stat.executeQuery()
    var category = "Not found"
    if(rs.next()) {
      category = rs.getString("category")
      rs.close()
      connection.close()
    }
    val result = new Category(category)
    result.id = categoryID
    result
  }

  def makeBookFromResultSet(rs: ResultSet): Book = {
    val path = rs.getString("path_to_content")
    val title = rs.getString("title")
    val category_id = rs.getInt("category_id")
    val category = getCategoryByID(category_id)
    val description = rs.getString("description")
    val author_name = rs.getString("name")
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

  def findBookByTitleAndAuthor(title: String, authorName: String) = {
    val connection = prepareConnection()
    val stat = statBld.findBookIDByTitleAndAuthorStatement(connection)
    stat.setString(1, title)
    stat.setString(2, authorName)
    val rs = stat.executeQuery()
    var id = -1
    while (rs.next()) {
      id = rs.getInt("id")
    }
    rs.close()
    connection.close()
    id
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
      authors = authors :+ makeAuthorFromResultSet(rs)
    }
    rs.close()
    connection.close()
    authors
  }

  def addAuthor(author: Author) {
    val connection = prepareConnection()
    println(author.getName)
    val authorFromDB = findAuthor(author.getName)
    if (authorFromDB.getName == "Unknown") {
      println("Author not found, adding.")
      val stat = statBld.addAuthorStatement(connection)
      stat.setString(1, author.getName)
      stat.setString(2, author.getAdditionalInfo)
      stat.executeUpdate()

      val rs = stat.getGeneratedKeys()
      if (rs.next()){
        author.id = rs.getInt(1)
      }
      println("Last row id")

    }
    else
      author.id = authorFromDB.id
    println(author.id)
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
        val id = rs.getInt("id")
        val result =  new Author(name, additional_info)
        result.id = id
        result
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
    stat.executeUpdate(
      """
        create table if not exists books
        (
          id integer primary key autoincrement,
          title string,
          path_to_content string,
          description string,
          category_id integer
        );

        create table if not exists authors
        (
          id integer primary key autoincrement,
          name string unique,
          additional_info string
        );

        create table if not exists book_authors
        (
          id integer primary key autoincrement,
          book_id integer,
          author_id integer
        );

        create table if not exists categories
        (
          id integer primary key autoincrement,
          category string unique
        );

        create table if not exists tags
        (
          id integer primary key autoincrement,
          tag string unique
        );

        create table if not exists book_tags
        (
          id integer primary key autoincrement,
          book_id integer,
          tag_id integer
        );
      """.stripMargin)
    connection.close()
  }

  def dropAllTables() {
    val connection = prepareConnection()
    val stat = connection.createStatement()
    stat.executeUpdate("drop table books")
    stat.executeUpdate("drop table authors")
  }
}
