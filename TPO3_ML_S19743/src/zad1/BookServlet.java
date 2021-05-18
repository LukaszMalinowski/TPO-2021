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

@WebServlet (name = "BookServlet", urlPatterns = {"/book", "/book/"})
public class BookServlet extends HttpServlet {

    private BookRepository repository;

    @Override
    public void init() throws ServletException {
        super.init();
        repository = new BookRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.write("<!DOCTYPE html>\n" +
                             "<html lang=\"en\">\n" +
                             "<head>\n" +
                             "    <meta charset=\"UTF-8\">\n" +
                             "    <title>TPO</title>\n" +
                             "</head>\n" +
                             "<body>\n");

        


        writer.write("</body>\n" +
                             "</html>");
    }

}
