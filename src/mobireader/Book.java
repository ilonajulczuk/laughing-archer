
package mobireader;

import hello.Author;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author att
 */
public class Book {
    String title;
    Author author;
    Date timestamp;
    String content = "";
    String pathToContent = "";
    Boolean isContentFetched = false;
    public String description = "";
    public String category = "";

    public String toString() {
        return title + " written by " + author.getName();
    }

    public Book(String title, Author author, Date timestamp) {
        this.title = title;
        this.author = author;
        this.timestamp = timestamp;
    }

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
        this.timestamp = new Date();
    }

    public Book(String title, Author author, String pathToContent) {
        this.title = title;
        this.author = author;
        this.timestamp = new Date();
        this.pathToContent = pathToContent;
    }

    public Book(String title, Author author, String pathToContent, String description, String category) {
        this.title = title;
        this.author = author;
        this.timestamp = new Date();
        this.pathToContent = pathToContent;
        this.category = category;
        this.description = description;
    }

    public String detailedDescription() {
        return "Title: " + title + "\n" +
                description + "\n" +
                "written by: " + author.getName() + "\n" +
                "located in: " + pathToContent + "\n" +
                "classified as: " + category + "\n" +
                "added: " + timestamp.toString() + "\n";
    }

    public String getContent() {
        return this.content;
    }

    public String getPathToContent() {
        return this.pathToContent;
    }

    public Boolean isContentAvailable() {
        return isContentFetched;
    }

    public String getTitle() {
        return this.title;
    }

    public Author getAuthor() {
        return this.author;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    static public Book exemplaryBook() {
        return new Book("Hakuna Matata",
                new Author("Mr. Elephant", "Hakuna Matata"));
    }

    static public ArrayList<Book> createSomeExamples() {
        ArrayList<Book> books = new ArrayList<Book>();
        books.add(new Book("Hakuna Matata", new Author("Mr. Elephant", "Hakuna Matata")));
        books.add(new Book("Life of a star", new Author("Elvis Presley", "Life of a star")));
        String[] titles = {"Real physics", "Kartofle"};
        books.add(new Book("Real physics", new Author("Wise old physicist",
                new ArrayList<String>(Arrays.asList(titles)))));
        return books;
    }
}
