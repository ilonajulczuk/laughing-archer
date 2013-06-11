package db

import java.sql.Connection



class TagStatementBuilder extends StatementBuilder {

  def addTagStatement(connection: Connection) =
    prepareStatement(connection,"insert into tags(tag) values(?)")

  def getAllTagsStatement(connection: Connection) =
    prepareStatement(connection,"select * from tags")

  def removeTagStatement(connection: Connection) =
    prepareStatement(connection,"delete from tags where id = ?")

  def findTagStatement(connection: Connection) =
    prepareStatement(connection,"select * from tags where tag = ?")

  def findTagsByBookIdStatement(connection: Connection) =
    prepareStatement(connection,"select t.id, t.tag from book_tags r left join tags t on" +
      " r.tag_id = t.id where r.book_id = ? ")

}
