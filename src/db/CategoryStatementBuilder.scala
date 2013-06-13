package db

import java.sql.Connection


/**
 *CategoryStatementBuilder builds sql statements for categories which are later used
 * in DB handler. DB handler has an instance of CategoryStatementBuilder.
 */

class CategoryStatementBuilder extends StatementBuilder {

  def addCategoryStatement(connection: Connection) =
    prepareStatement(connection,"insert into categories(category) values(?)")


  def findCategoryByIDStatement(connection: Connection) =
    prepareStatement(connection, "select category from categories where id = ?")

  def findCategoryStatement(connection: Connection) =
    prepareStatement(connection, "select id from categories where category = ?")

}
