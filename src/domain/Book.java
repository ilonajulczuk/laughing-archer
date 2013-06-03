
package domain;

import java.util.Date;
import java.util.ArrayList;


public class Book {
    String title;
    Author author;
    public Integer id = -1;
    Date timestamp;
    public ArrayList<Tag> tags = new ArrayList<>();
    String pathToContent = "";
    public String description = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!author.equals(book.author)) return false;
        if (!category.equals(book.category)) return false;
        if (!description.equals(book.description)) return false;
        if (!id.equals(book.id)) return false;
        if (!pathToContent.equals(book.pathToContent)) return false;
        if (!title.equals(book.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + pathToContent.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    public Category category;

    public String toString() {
        return title + " written by " + author.getName();
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

    public Book(String title, Author author, String pathToContent, String description, Category category) {
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


    public String getPathToContent() {
        return this.pathToContent;
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
}
