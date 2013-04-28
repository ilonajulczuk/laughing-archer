/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

/**
 *
 * @author att
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mobireader.Book;

class StatementBuilder
{
    private PreparedStatement prepareStatement(Connection connection, String statementText) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(
               statementText);
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        return statement;
    }
    public PreparedStatement findAuthorByNameStatement(Connection connection) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(
                "select * from authors where name=?");
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        return statement;
    }
    
    public PreparedStatement addAuthorStatement(Connection connection) 
            throws SQLException, ClassNotFoundException
    {
        String statementText;
        statementText = "insert or replace into authors(name, additional_info) values( ?, ?)";
        return prepareStatement(connection, statementText);
    }
    
    
    public PreparedStatement addBookStatement(Connection connection) 
            throws SQLException, ClassNotFoundException
    {
        String statementText = "insert or replace into books(title, author_name, path_to_content) values( ?, ?, ?)";
        return prepareStatement(connection, statementText);
    }
    
    public PreparedStatement findBookStatement(Connection connection) 
            throws SQLException, ClassNotFoundException
    {
        String statementText = "select b.title, b.author_name, b.path_to_content,"
                + " a.additional_info from books b left join authors a on"
                + " b.author_name = a.name where b.title=?";
        return prepareStatement(connection, statementText);
    }
    
    public PreparedStatement findBookByAuthorStatement(Connection connection) 
            throws SQLException, ClassNotFoundException
    {
        String statementText = "select * from books where author_name=?";
        return prepareStatement(connection, statementText);
    }
    
    public PreparedStatement getAllBooksStatement(Connection connection) 
            throws SQLException, ClassNotFoundException
    {
        String statementText = "select b.title, b.author_name, b.path_to_content,"
                + " a.additional_info from books b left join authors a on"
                + " b.author_name = a.name";
        return prepareStatement(connection, statementText);
    }
    
}

public class SqliteDatabaseHandler
{
    String dbFile = "sample.db";
    String driver = "org.sqlite.JDBC";
    StatementBuilder statBld = new StatementBuilder();
    public SqliteDatabaseHandler(String databaseFile)
    {
        this.dbFile = databaseFile;
    }
    
    public SqliteDatabaseHandler()
    {
        this.dbFile = "sample.db";
    }
    
    public Connection prepareConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName(this.driver);
        return DriverManager.getConnection("jdbc:sqlite:" + dbFile);
    }
    
    
    public  void addBooks(ArrayList<Book> books) throws ClassNotFoundException
    {
        for(Book book : books)
        {
            addBook(book);
        }
    }
    
    public  void addBook(Book book) throws ClassNotFoundException
    {
       try
        {
            Connection connection = prepareConnection();
            try
            {
                Author author = this.findAuthor(book.getAuthor().getName());
                if(author == null)
                {
                    this.addAuthor(book.getAuthor());
                }
                PreparedStatement stat;
                stat= statBld.addBookStatement(connection);
                stat.setString(1, book.getTitle());
                stat.setString(2, book.getAuthor().getName());
                stat.setString(3, book.getPathToContent());
                stat.executeUpdate();
            }
            catch(SQLException e) { System.err.println(e.getMessage()); }
            finally
            {
              try
              {
                if(connection != null)
                  connection.close();
              }
              catch(SQLException e) { System.err.println(e.getMessage()); }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Problem occured during making connection to db");
            System.err.println(e.getMessage());     
        }
    }
    
    public Book findBook(String title) throws ClassNotFoundException
    {
        try
        {
            Connection connection = prepareConnection();
            try
            {
                PreparedStatement stat;
                stat= statBld.findBookStatement(connection);
                stat.setString(1, title);
                stat.executeQuery();
                ResultSet rs = stat.executeQuery();
                String path = rs.getString("path_to_content");
                String author_name = rs.getString("author_name");
                String info_about_author = rs.getString("additional_info");
                Author author = new Author(author_name, info_about_author);
                rs.close();
                return new Book(title, author, path);
            }
            catch(SQLException e) { System.err.println(e.getMessage()); }
            finally
            {
              try
              {
                if(connection != null)
                  connection.close();
              }
              catch(SQLException e) { System.err.println(e.getMessage()); }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Problem occured during making connection to db");
            System.err.println(e.getMessage());     
        }
        return null;
    }
    
    public ArrayList<Book> findBooksByAuthor(Author author) throws ClassNotFoundException
    {
        try
        {
            Connection connection = prepareConnection();
            try
            {
                PreparedStatement stat;
                stat = statBld.findBookByAuthorStatement(connection);
                stat.setString(1, author.getName());
                ResultSet rs = stat.executeQuery();
                ArrayList<Book> books = new ArrayList<>();
                
                while(rs.next())
                {
                    String path = rs.getString("path_to_content");
                    String title = rs.getString("title");
                    books.add(new Book(title, author, path));
                }
                rs.close();
                return books;
            }
            catch(SQLException e) { System.err.println(e.getMessage()); }
            finally
            {
              try
              {
                if(connection != null)
                  connection.close();
              }
              catch(SQLException e) { System.err.println(e.getMessage()); }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Problem occured during making connection to db");
            System.err.println(e.getMessage());     
        }
        return null;
    }
    
    public ArrayList<Book> getAllBooks() throws ClassNotFoundException
    {
        try
        {
            Connection connection = prepareConnection();
            try
            {
                PreparedStatement stat;
                stat = statBld.getAllBooksStatement(connection);
                ResultSet rs = stat.executeQuery();
                ArrayList<Book> books = new ArrayList<>();
                while(rs.next())
                {
                    String path = rs.getString("path_to_content");
                    String title = rs.getString("title");
                    String author_name = rs.getString("author_name");
                    String info_about_author = rs.getString("additional_info");
                    Author author = new Author(author_name, info_about_author);
                    books.add(new Book(title, author, path));
                }
                rs.close();
                return books;
            }
            catch(SQLException e) { System.err.println(e.getMessage()); }
            finally
            {
              try
              {
                if(connection != null)
                  connection.close();
              }
              catch(SQLException e) { System.err.println(e.getMessage()); }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Problem occured during making connection to db");
            System.err.println(e.getMessage());     
        }
        return null;
    }
    
    public  void addAuthor(Author author) throws ClassNotFoundException
    {
       try
        {
            Connection connection = prepareConnection();
            try
            {
                PreparedStatement stat;
                stat= statBld.addAuthorStatement(connection);
                stat.setString(1, author.getName());
                stat.setString(2, author.getAdditionalInfo());
                stat.executeUpdate();
            }
            catch(SQLException e) { System.err.println(e.getMessage()); }
            finally
            {
              try
              {
                if(connection != null)
                  connection.close();
              }
              catch(SQLException e) { System.err.println(e.getMessage()); }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Problem occured during making connection to db");
            System.err.println(e.getMessage());     
        }
    }
    
    
    
    public Author findAuthor(String name) throws ClassNotFoundException
    {
        try
        {
            Connection connection = prepareConnection();
            try
            {
                PreparedStatement stat;
                stat= statBld.findAuthorByNameStatement(connection);
                stat.setString(1, name);
                stat.executeQuery();
                ResultSet rs = stat.executeQuery();
                String additional_info = rs.getString("additional_info");
                rs.close();
                return new Author(name, additional_info);
            }
            catch(SQLException e) { System.err.println(e.getMessage()); }
            finally
            {
              try
              {
                if(connection != null)
                  connection.close();
              }
              catch(SQLException e) { System.err.println(e.getMessage()); }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Problem occured during making connection to db");
            System.err.println(e.getMessage());     
        }
        return null;
    }
    
    public void createTablesInDB() throws SQLException, ClassNotFoundException
    {
        try
        {
            Connection connection = prepareConnection();
            try
            {
                Statement stat = connection.createStatement();
                stat.executeUpdate("drop table if exists books");
                stat.executeUpdate("drop table if exists authors");
                stat.executeUpdate("create table books (title string, " +
                                   "author_name string, path_to_content string)");
                stat.executeUpdate("create table authors (name string unique, " +
                                   "additional_info string)");
                
            }
            catch(SQLException e) { System.err.println(e.getMessage()); }
            finally
            {
              try
              {
                if(connection != null)
                  connection.close();
              }
              catch(SQLException e) { System.err.println(e.getMessage()); }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Problem occured during making connection to db");
            System.err.println(e.getMessage());     
        }
    }
    
    public static void main () throws Exception
    {// load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC");

    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      
      statement.executeUpdate("drop table if exists books");
      statement.executeUpdate("drop table if exists authors");
      statement.executeUpdate("create table books (id integer, title string, " +
              "author_id integer, path_to_content string)");
      statement.executeUpdate("create table authors (id integer, name string, " +
              "additional_info string)");
        
      statement.executeUpdate("insert into books values(1, 'leo', 1, '/somepath/leo.txt')");
      statement.executeUpdate("insert into books values(2, 'yui', 1, '/somepath/yui.txt')");
      ResultSet rs = statement.executeQuery("select * from books");
      rs.close();
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }
}
