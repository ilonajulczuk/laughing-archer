package hello

import java.sql.{Statement, Connection}

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
      + " r.book_id = b.id left join authors a on r.author_id = a.id where b.title=?")

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

