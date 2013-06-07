package db

import java.sql.Connection

class PrioritizedBookStatementBuilder extends StatementBuilder {


  def getAllPrioritizedBook(connection: Connection) =
    prepareStatement(connection, "select * from ")

}
