import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletContext;

import java.util.*;

import com.google.gson.Gson;

@WebServlet(
    name = "ListFilesServlet",
    urlPatterns = {"/listFiles"}
    )

public class ListFilesServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException {

    //Obtener path de los archivos
    ServletContext context = getServletContext();
    Set<String> files = context.getResourcePaths("/WEB-INF/resources");
    PrintWriter writer = res.getWriter();

    //Obtener nombre de los archivos
    String filesName[] = files.stream()
    .map(f -> new File(f).getName())
    .filter(f -> f.toLowerCase().endsWith(".txt"))
    .toArray(String[]::new);

    //Convertir a json
    String json = new Gson().toJson(filesName);

    //Construir respuesta
   res.setContentType("application/json");
   res.setCharacterEncoding("UTF-8");
   writer.print(json);
   writer.flush();
   }
}

//https://docs.oracle.com/javaee/1.3/api/javax/servlet/ServletContext.html#getResourcePaths%28java.lang.String%29
//https://stackoverflow.com/questions/14526260/how-do-i-get-the-file-name-from-a-string-containing-the-absolute-file-path
