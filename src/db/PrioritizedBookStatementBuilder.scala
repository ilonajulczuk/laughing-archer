package db

import java.sql.Connection

/**
 * PrioritizedBookStatementBuilder builds sql statements for prioritized which are later used
 * in DB handler. DB handler has an instance of PrioritizedBookStatementBuilder.
 *
 * Table of prioritized books looks like this:
 *(
    id integer primary key autoincrement,
    book_id integer,
    priority integer,
    deadline string
    );
 */

class PrioritizedBookStatementBuilder extends StatementBuilder {

  def getAllPrioritizedBooks(connection: Connection) =
    prepareStatement(connection, "select p.id, p.priority, p.deadline, b.title from priority_books p left join books b on" +
      " p.book_id = b.id ")

  def addPrioritizedBook(connection: Connection) =
    prepareStatement(connection, "insert into " +
      "priority_books(book_id, priority, deadline) values( ?, ?, ?)")

  def removePrioritizedBook(connection: Connection) =
    prepareStatement(connection, "delete from priority_books where book_id = ?")
}
