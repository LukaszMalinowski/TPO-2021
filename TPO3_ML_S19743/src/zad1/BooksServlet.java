package zad1;

import zad1.model.Book;
import zad1.repository.BookRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet (name = "BooksServlet", urlPatterns = {"/books", "/books/"})
public class BooksServlet extends HttpServlet {

    private BookRepository repository;
    private PrintWriter writer;

    @Override
    public void init() throws ServletException {
        super.init();
        repository = new BookRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        writer = resp.getWriter();

        writer.write("<!DOCTYPE html>\n" +
                             "<html lang=\"en\">\n" +
                             "<head>\n" +
                             "    <meta charset=\"UTF-8\">\n" +
                             "    <title>TPO</title>\n" +
                             "</head>\n" +
                             "<body>\n" +
                             "\n" +
                             "<h1>All books:</h1>\n" +
                             "<ul>");

        List<Book> allBooks = repository.getAllBooks();

        allBooks.forEach(this::addBookToList);

        writer.write("</ul>\n" +
                             "\n" +
                             "</body>\n" +
                             "</html>");
    }

    private void addBookToList(Book book) {
        String listItem = String.format("<li>\n" +
                                                "        <h2>%s</h2>\n" +
                                                "        <h3>%s</h3>\n", book.getTitle(), book.getAuthor());

        if (book.getDescription() != null) {
            listItem += String.format("<p>%s</p>\n" +
                                              "    </li>", book.getDescription());
        }

        writer.write(listItem);
    }
}
