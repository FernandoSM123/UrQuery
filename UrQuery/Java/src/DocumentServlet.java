import java.io.IOException;
import java.io.PrintWriter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletContext;

import java.util.stream.*;

@WebServlet(
        name = "DocumentServlet",
        urlPatterns = {"/document"}
    )

public class DocumentServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
    
    //Leer archivo
    String id = req.getParameter("id");
    String filename = "/WEB-INF/resources/"+id;
    ServletContext context = getServletContext();
    String text = "";
    InputStream inp = context.getResourceAsStream(filename);
    PrintWriter writer = res.getWriter();

    if (inp != null) {
    InputStreamReader isr = new InputStreamReader(inp);
    BufferedReader reader = new BufferedReader(isr);
    String lines = reader.lines().collect(Collectors.joining("\n"));

     // Construir respuesta
     res.setContentType("text/html");
     res.setCharacterEncoding("UTF-8");
     writer.print(lines);
     writer.flush();
    }
    }
}
