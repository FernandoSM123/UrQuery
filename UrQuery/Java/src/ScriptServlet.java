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

//XML
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.util.stream.*;

@WebServlet(name = "scriptServlet", urlPatterns = { "/script" })

public class ScriptServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        // //Leer archivo
        String id = req.getParameter("id");
        String filename = "/WEB-INF/resources/scripts/" + id;
        ServletContext context = getServletContext();
        String text = "";

        InputStream inp = context.getResourceAsStream(filename);
        PrintWriter writer = res.getWriter();

        if (inp != null) {
            InputStreamReader isr = new InputStreamReader(inp);
            BufferedReader reader = new BufferedReader(isr);
            String lines = reader.lines().collect(Collectors.joining("\n"));

            // Construir respuesta
            res.setContentType("plain/text");
            res.setCharacterEncoding("UTF-8");
            writer.print(lines);
            writer.flush();
        }
    }
}
