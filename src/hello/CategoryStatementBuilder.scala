package hello

import java.sql.Connection

/**
 * Created with IntelliJ IDEA.
 * User: att
 * Date: 6/7/13
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
class CategoryStatementBuilder extends StatementBuilder {

  def addCategoryStatement(connection: Connection) =
    prepareStatement(connection,"insert into categories(category) values(?)")


  def findCategoryByIDStatement(connection: Connection) =
    prepareStatement(connection, "select category from categories where id = ?")

  def findCategoryStatement(connection: Connection) =
    prepareStatement(connection, "select id from categories where category = ?")

}
