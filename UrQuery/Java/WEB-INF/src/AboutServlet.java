import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

    //Leer archivo
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    String filename = "/WEB-INF/resources/about.json";
    ServletContext context = getServletContext();
    String text = "";
    InputStream inp = context.getResourceAsStream(filename);

    if (inp != null) {
    InputStreamReader isr = new InputStreamReader(inp);
    BufferedReader reader = new BufferedReader(isr);
    PrintWriter pw = res.getWriter();
    String line = "";
  
    while ((line = reader.readLine()) != null) {
    text += line;
    }

     // Construir respuesta
     PrintWriter writer = res.getWriter();
     writer.print(text);
     writer.flush();
    }
    }
}
