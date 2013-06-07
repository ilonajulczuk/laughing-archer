package hello

import java.sql.{Statement, Connection}

class StatementBuilder {
  protected def prepareStatement(connection: Connection,
                               statementText: String) = {
    val statement = connection.prepareStatement(
      statementText, Statement.RETURN_GENERATED_KEYS)
    statement.setQueryTimeout(30) // set timeout to 30 sec.
    statement
  }





}

