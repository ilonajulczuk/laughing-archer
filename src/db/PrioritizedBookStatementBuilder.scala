package db

import java.sql.Connection

class PrioritizedBookStatementBuilder extends StatementBuilder {

 /* create table if not exists priority_books
  (
    id integer primary key autoincrement,
    book_id integer,
    priority integer,
    deadline string
    );*/

  def getAllPrioritizedBooks(connection: Connection) =
    prepareStatement(connection, "select p.priority, p.deadline, b.title from priority_books p left join books b on" +
      " p.book_id = b.id ")

  def addPrioritizedBook(connection: Connection) =
    prepareStatement(connection, "insert into " +
      "priority_books(book_id, priority, deadline) values( ?, ?, ?)")

}
