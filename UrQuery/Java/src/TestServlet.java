// import java.io.IOException;
// import java.io.PrintWriter;
// import jakarta.servlet.*;
// import jakarta.servlet.http.*;
// import javax.servlet.annotation.WebServlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletContext;

@WebServlet(
        name = "TestServlet",
        urlPatterns = {"/test"}
    )

public class TestServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        try {
            res.setContentType("text/html");
            PrintWriter pw = res.getWriter();

            pw.println("<html><body>");
            pw.println("Welcome to servlet");
            pw.println("</body></html>");

            pw.close();
        } catch (Throwable t) {
            throw new IOException(t);
        }
    }
}