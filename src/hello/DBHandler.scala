package hello


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scala.collection.JavaConversions._

import mobireader.Book;

class StatementBuilderA
{
	private def prepareStatement(connection: Connection , statementText: String) =
		{
		val statement = connection.prepareStatement(
				statementText);
		statement.setQueryTimeout(30);  // set timeout to 30 sec.
		statement
		}

	def findAuthorByNameStatement(connection: Connection) = 
			prepareStatement(connection, "select * from authors where name=?")

			def addAuthorStatement(connection: Connection) =
			prepareStatement(connection,"insert or replace into " +
					"authors(name, additional_info) values( ?, ?)")        

					def addBookStatement(connection: Connection) =  
					prepareStatement(connection, "insert or replace into books(title, author_name," +
							" path_to_content) values( ?, ?, ?)")

							def findBookStatement(connection: Connection) =
							prepareStatement(connection, "select b.title, b.author_name, b.path_to_content,"
									+ " a.additional_info from books b left join authors a on"
									+ " b.author_name = a.name where b.title=?")

									def findBookByAuthorStatement(connection: Connection) =
									prepareStatement(connection, "select * from books where author_name=?")

									def getAllBooksStatement(connection: Connection) =
									prepareStatement(connection, "select b.title, b.author_name, b.path_to_content,"
											+ " a.additional_info from books b left join authors a on"
											+ " b.author_name = a.name")
}

class DBHandler(databaseFile: String)
{
	val dbFile = "sample.db";
	val driver = "org.sqlite.JDBC";
	val statBld = new StatementBuilderA();

	def this() = {
		this("sample.db")
	}

	def prepareConnection() = {
		Class.forName(this.driver);
		DriverManager.getConnection("jdbc:sqlite:" + dbFile);
	}

	import scala.collection.JavaConverters._

	def addBooks(books: ArrayList[Book]) = {
		val list = books.asScala.toList
				list.foreach(addBook)
	}

	def addBook(book: Book) = {
		val connection = prepareConnection();
		val author = findAuthor(book.getAuthor().getName());
		if(author.name == "Unknown")
		{
			this.addAuthor(book.getAuthor());
		}
		val stat= statBld.addBookStatement(connection);
		stat.setString(1, book.getTitle());
		stat.setString(2, book.getAuthor().getName());
		stat.setString(3, book.getPathToContent());
		stat.executeUpdate();

		connection.close()
	}

	def findBook(title: String) = {
		val connection = prepareConnection();
		val stat= statBld.findBookStatement(connection);
		stat.setString(1, title);
		stat.executeQuery();
		val rs = stat.executeQuery();
		val path = rs.getString("path_to_content");
		val author_name = rs.getString("author_name");
		val info_about_author = rs.getString("additional_info");
		val author = new Author(author_name, info_about_author);
		rs.close();
		new Book(title, author, path);
		connection.close()
	}

	def findBooksByAuthor(author: Author) = {
		val connection = prepareConnection()
				val stat = statBld.findBookByAuthorStatement(connection);
		stat.setString(1, author.getName());
		val rs = stat.executeQuery();
		val books = new ArrayList[Book]();
		while(rs.next())
		{
			val path = rs.getString("path_to_content")
					val title = rs.getString("title")
					books.add(new Book(title, author, path))
		}
		rs.close()
		connection.close()
		books
	}

	def getAllBooks() =
		{
		val connection = prepareConnection();

		val stat = statBld.getAllBooksStatement(connection);
		val rs = stat.executeQuery();
		val books = new ArrayList[Book]();
		while(rs.next())
		{
			val path = rs.getString("path_to_content");
			val title = rs.getString("title");
			val author_name = rs.getString("author_name");
			val info_about_author = rs.getString("additional_info");
			val author = new Author(author_name, info_about_author);
			books.add(new Book(title, author, path));
		}
		rs.close();
		connection.close()
		books;
		}

	def addAuthor(author: Author) = {
		val connection = prepareConnection();
		val stat = statBld.addAuthorStatement(connection);
		stat.setString(1, author.getName());
		stat.setString(2, author.getAdditionalInfo());
		stat.executeUpdate();

		connection.close()
	}

	def findAuthor(name: String) = {
		try {
			val connection = prepareConnection()
					val stat = statBld.findAuthorByNameStatement(connection);
			stat.setString(1, name);
			stat.executeQuery();
			val rs = stat.executeQuery();
			val author = if(rs.next()) {
				val additional_info = rs.getString("additional_info")
						new Author(name, additional_info)
			}
			else {
				new Author("Unknown")
			}	
			rs.close();
			connection.close()
			author
		}
		catch {
		case ex: SQLException => {
			println("SQLException: " + ex.getSQLState())
			new Author("Unknown")
		}
		}
	}

	def createTablesInDB() = {
		val connection = prepareConnection();
		val stat = connection.createStatement();
		stat.executeUpdate("drop table if exists books");
		stat.executeUpdate("drop table if exists authors");
		stat.executeUpdate("create table books (title string, " +
				"author_name string, path_to_content string)");
		stat.executeUpdate("create table authors (name string unique, " +
				"additional_info string)");
		connection.close()
	}

	def main () =
		{// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");


		val connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
		val statement = connection.createStatement();
		statement.setQueryTimeout(30);  // set timeout to 30 sec.

		statement.executeUpdate("drop table if exists books");
		statement.executeUpdate("drop table if exists authors");
		statement.executeUpdate("create table books (id integer, title string, " +
				"author_id integer, path_to_content string)");
		statement.executeUpdate("create table authors (id integer, name string, " +
				"additional_info string)");

		statement.executeUpdate("insert into books values(1, 'leo', 1, '/somepath/leo.txt')");
		statement.executeUpdate("insert into books values(2, 'yui', 1, '/somepath/yui.txt')");
		val rs = statement.executeQuery("select * from books");
		rs.close();
		connection.close()


		}
}
