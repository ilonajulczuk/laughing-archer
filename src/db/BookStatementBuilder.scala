package db

import java.sql.Connection


class BookStatementBuilder extends StatementBuilder {

  def addBookStatement(connection: Connection) =
    prepareStatement(connection, "insert into books(title," +
      " path_to_content, description, category_id) values( ?, ?, ?, ?)")

  def addBookAuthorRelation(connection: Connection) =
    prepareStatement(connection, "insert into book_authors(book_id, author_id)" +
      " values( ?, ?)")

  def addBookTagRelation(connection: Connection) =
    prepareStatement(connection, "insert into book_tags(book_id, tag_id)" +
      " values( ?, ?)")

  def removeBookStatement(connection: Connection) =
    prepareStatement(connection, "delete from books where id = ?")

  def removeBookAuthorRelationStatement(connection: Connection) =
    prepareStatement(connection, "delete from book_authors where book_id = ? and author_id = ?")

  def removeBookTagRelationStatement(connection: Connection) =
    prepareStatement(connection, "delete from book_tags where book_id = ? and tag_id = ?")

  def findBookStatement(connection: Connection) =
    prepareStatement(connection, "select b.id, b.title, a.name," +
      " b.path_to_content, b.description, b.category_id,"
      + " a.additional_info from books b left join book_authors r on"
      + " r.book_id = b.id left join authors a on r.author_id = a.id where b.title=?")

  def findBookByIdStatement(connection: Connection) =
    prepareStatement(connection, "select b.id, b.title, a.name," +
      " b.path_to_content, b.description, b.category_id,"
      + " a.additional_info from books b left join book_authors r on"
      + " r.book_id = b.id left join authors a on r.author_id = a.id where b.id=?")

  def findBookByCategoryStatement(connection: Connection) =
    prepareStatement(connection, "select b.id, b.title, a.name, b.path_to_content," +
      " b.description, b.category_id,"
      + " a.additional_info from books b left join authors a on"
      + " b.author_name = a.name where b.category=?")

  def findBookByAuthorStatement(connection: Connection) =
    prepareStatement(connection, "select b.id, b.title, a.name, b.path_to_content, " +
      "b.description, b.category_id, a.additional_info from books b left join book_authors r" +
      " on b.id = r.book_id left join authors a on r.author_id = a.id where a.id=?")

  def getAllBooksStatement(connection: Connection) =
    prepareStatement(connection, "select b.id, b.title, b.path_to_content," +
      " b.category_id, b.description,"
      + " a.name, a.additional_info from books b left join book_authors r on"
      + " r.book_id = b.id left join authors a on a.id = r.author_id")

  def findBookIDByTitleAndAuthorStatement(connection: Connection) =
    prepareStatement(connection, "select b.id"
      + " from books b left join book_authors r on"
      + " r.book_id = b.id left join authors a on r.author_id = a.id where b.title=? and a.name=?")
}
