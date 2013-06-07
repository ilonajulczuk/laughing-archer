package db

import java.sql.Connection

class AuthorStatementBuilder extends StatementBuilder {
  def findAuthorByNameStatement(connection: Connection) =
    prepareStatement(connection, "select * from authors where name=?")

  def addAuthorStatement(connection: Connection) =
    prepareStatement(connection, "insert into " +
      "authors(name, additional_info) values( ?, ?)")

  def removeAuthorStatement(connection: Connection) =
    prepareStatement(connection, "delete from authors where name =" +
      "  ?")

  def getAllAuthorsStatement(connection: Connection) =
    prepareStatement(connection, "select * from authors")
}
