/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stuff;

import java.sql.SQLException;

import hello.DBHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author att
 */
public class SqlConnectionJUnitTest {
    DBHandler handler = new DBHandler();

    public SqlConnectionJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        handler.createTablesInDB();
    }

    @After
    public void tearDown() {
    }
    
    
    /* Those test aren't compatible with scala version of this class
    @Test
    public void testAddingBookToDb() throws Exception
    {
        String title = "Title";
        String authorName = "Gepetto";
        Author gepetto = new Author(authorName, title);
        String path = "/somepath/book.txt";
        Book bookToBeAdded = new Book(title, gepetto, path);
        handler.addBook(bookToBeAdded);
        Book bookFromDB = handler.findBook(title);
        assertEquals(title, bookFromDB.getTitle());
        assertEquals(authorName, bookFromDB.getAuthor().getName());
        assertEquals(path, bookFromDB.getPathToContent());
    }
    
    
    @Test
    public void testAddingFewBooksOfSameAuthorToDb() throws ClassNotFoundException
    {
        String title1 = "Title1";
        String title2 = "Title2";
        String title3 = "Title3";
        String authorName = "Gepetto";
        Author gepetto = new Author(authorName);
        String path = "/somepath/book.txt";
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book(title1, gepetto, path));
        books.add(new Book(title2, gepetto, path));
        books.add(new Book(title3, gepetto, path));
        //handler.addBooks(books);
        //ArrayList<Book> booksFromDB = handler.findBooksByAuthor(gepetto);
        assertEquals(books.size(), booksFromDB.size());
        for(int i = 0; i < books.size(); i++ )
        {
            assertEquals(books.get(i).getTitle(), booksFromDB.get(i).getTitle());
            assertEquals(books.get(i).getAuthor().getName(), booksFromDB.get(i).getAuthor().getName());
        }
        
    }       
    
    @Test
    public void testGettingAllBooksFromDb() throws ClassNotFoundException
    {
        String title1 = "Title1";
        String title2 = "Title2";
        String title3 = "Title3";
        String authorName1 = "Gepetto";
        String authorName2 = "Geronimo";
        Author gepetto = new Author(authorName1);
        Author geronimo = new Author(authorName2);
        String path = "/somepath/book.txt";
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book(title1, gepetto, path));
        books.add(new Book(title2, gepetto, path));
        books.add(new Book(title3, gepetto, path));
        books.add(new Book(title1, geronimo, path));
        books.add(new Book(title2, geronimo, path));
        books.add(new Book(title3, geronimo, path));
        handler.addBooks(books);
        ArrayList<Book> booksFromDB = handler.getAllBooks();
        assertEquals(books.size(), booksFromDB.size());
        for(int i = 0; i < books.size(); i++ )
        {
            assertEquals(books.get(i).getTitle(), booksFromDB.get(i).getTitle());
            assertEquals(books.get(i).getAuthor().getName(), booksFromDB.get(i).getAuthor().getName());
        }
    }
     
    @Test
    public void testAddingAuthorToDb() throws Exception
    {
        String authorName = "Geronimo";
        String additionalInfo = "Likes wood";
        Author authorToBeAdded = new Author(authorName, additionalInfo);
        handler.addAuthor(authorToBeAdded);
        Author authorFromDB = handler.findAuthor(authorName);
        assertEquals(authorName, authorFromDB.getName());
        assertEquals(additionalInfo, authorFromDB.getAdditionalInfo());
    }
    
    
    @Test
    public void testAddingTheSameAuthorToDbFewTimes() throws Exception
    {
        String authorName = "Geronimo";
        String additionalInfo = "Likes wood";
        Author authorToBeAdded = new Author(authorName, additionalInfo);
        handler.addAuthor(authorToBeAdded);
        String newerInfo = authorToBeAdded.getAdditionalInfo() +
                ", and trees, and art.";
        authorToBeAdded.addAdditionalInfo(newerInfo);
        handler.addAuthor(authorToBeAdded);
        Author anotherAuthor = new Author("Alfredo", "Enjoy waterfalls");
        handler.addAuthor(anotherAuthor);
        Author authorFromDB = handler.findAuthor(authorName);
        assertEquals(authorName, authorFromDB.getName());
        assertEquals(newerInfo, authorFromDB.getAdditionalInfo());
    }
    
    public void testAddingFewBooksToDB() throws Exception
    {
        ArrayList<Book> books = Book.createSomeExamples();
        handler.addBooks(books);
        for(Book book : books)
        {
            Book bookFromDB = handler.findBook(book.getTitle());
            assertTrue(bookFromDB != null);
        }
    }
    */
}