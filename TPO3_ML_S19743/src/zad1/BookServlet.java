package zad1;

import zad1.exception.BookNotFoundException;
import zad1.model.Book;
import zad1.repository.BookRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "BookServlet", urlPatterns = {"/book", "/book/"})
public class BookServlet extends HttpServlet {

    private BookRepository repository;

    private PrintWriter writer;

    private static final String AUTHOR_PARAM = "author";
    private static final String TITLE_PARAM = "title";

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
                             "<form method=\"GET\">\n" +
                             "        <label for=\"title\">Title:</label>\n" +
                             "        <input type=\"text\" id=\"title\" name=\"title\"><br>\n" +
                             "        <label for=\"author\">Author:</label>\n" +
                             "        <input type=\"text\" id=\"author\" name=\"author\"><br>\n" +
                             "        <input type=\"submit\" value=\"Search\">\n" +
                             "    </form>");

        String authorParam = req.getParameter(AUTHOR_PARAM);
        String titleParam = req.getParameter(TITLE_PARAM);

        if (authorParam != null && titleParam != null) {
            try {
                addBookFromDatabase(authorParam, titleParam);
            }
            catch (BookNotFoundException exception) {
                writer.write("<h3>Book not found.</h3>");
            }
        }

        writer.write("</body>\n" +
                             "</html>");
    }

    private void addBookFromDatabase(String authorParam, String titleParam) throws BookNotFoundException {
        Book book = null;

        if (!authorParam.isEmpty() && !titleParam.isEmpty()) {
            book = repository.getBookByAuthorAndTitle(authorParam, titleParam);
        }
        else if (authorParam.isEmpty() && !titleParam.isEmpty()) {
            book = repository.getBookByTitle(titleParam);
        }
        else if (!authorParam.isEmpty()) {
            book = repository.getBookByAuthor(authorParam);
        }

        if (book == null) {
            writer.write("<h3>Book not found.</h3>");
        }
        else {
            writer.write(String.format("<h2>%s</h2>\n" +
                                               "<h3>%s</h3>\n", book.getTitle(), book.getAuthor()));

            if (book.getDescription() != null) {
                writer.write(String.format("<p>%s</p>\n", book.getDescription()));
            } else {
                writer.write("<p>There's no description</p>");
            }
        }
    }

}
