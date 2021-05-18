package zad1.repository;

import zad1.exception.BookNotFoundException;
import zad1.model.Book;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private DataSource dataSource;

    public BookRepository() {
        try {
            Context initContext = new InitialContext();
            Context context = (Context)initContext.lookup("java:comp/env");
            dataSource = (DataSource)context.lookup("jdbc/tpo_books");
        }
        catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        Connection connection;
        List<Book> bookList = new ArrayList<>();
        try {
            connection = dataSource.getConnection();

            String query = "SELECT * FROM BOOKS";

            ResultSet resultSet = connection.createStatement().executeQuery(query);

            while (resultSet.next()) {
                long bookId = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");

                bookList.add(new Book(bookId, title, author, description));
            }

            connection.close();

        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        return bookList;
    }

    public Book getBookById(long id) throws SQLException, BookNotFoundException {
        Connection connection = dataSource.getConnection();

        String query = "SELECT * FROM BOOKS WHERE id = " + id;

        ResultSet resultSet = connection.createStatement().executeQuery(query);

        Book book;

        if (resultSet.next()) {
            long bookId = resultSet.getLong("id");
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            String description = resultSet.getString("description");

            book = new Book(bookId, title, author, description);
        }
        else {
            throw new BookNotFoundException(id);
        }

        connection.close();

        return book;
    }
}
