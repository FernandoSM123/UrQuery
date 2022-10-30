import java.io.IOException;
import java.io.PrintWriter;

import java.nio.file.Files;
import java.nio.file.Paths;

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

import java.util.Optional;


@WebServlet(
        name = "AboutServlet",
        urlPatterns = {"/about"}
    )

public class AboutServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

    //Leer archivo
    String filename = "/WEB-INF/resources/about.json";
    ServletContext context = getServletContext();
    String text = "";
    InputStream inp = context.getResourceAsStream(filename); //optional
    PrintWriter writer = res.getWriter();

    if (inp != null) {
    InputStreamReader isr = new InputStreamReader(inp);
    BufferedReader reader = new BufferedReader(isr);
    String lines = reader.lines().collect(Collectors.joining("\n"));
  

     // Construir respuesta
     res.setContentType("application/json");
     res.setCharacterEncoding("UTF-8");
     writer.print(lines);
     writer.flush();
    }

    }
}

/*
1- BufferedReader + Stream
https://mkyong.com/java8/java-8-stream-read-a-file-line-by-line/
*/
