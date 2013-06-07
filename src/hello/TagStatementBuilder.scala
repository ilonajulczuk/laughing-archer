package hello

import java.sql.Connection



class TagStatementBuilder extends StatementBuilder {

  def addTagStatement(connection: Connection) =
    prepareStatement(connection,"insert into tags(tag) values(?)")

}
