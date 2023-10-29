package tweb.pgtest;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import tweb.persistence.BasicPersistenceManager;
import tweb.persistence.PoolingPersistenceManager;

@WebServlet(name = "postgresTestServlet", value = "/pg-test-servlet")
public class PostgresTestServlet extends HttpServlet {

    public void init() { }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        //out.println(BasicPersistenceManager.getPersistenceManager().test());
        out.println(PoolingPersistenceManager.getPersistenceManager().test());
        out.println("</body></html>");
    }

    public void destroy() {
    }
}