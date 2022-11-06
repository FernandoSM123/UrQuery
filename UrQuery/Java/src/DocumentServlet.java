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

@WebServlet(name = "DocumentServlet", urlPatterns = { "/document" })

public class DocumentServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        // Leer archivo
        try {
            PrintWriter writer = res.getWriter();
            String id = req.getParameter("id");
            String filename = "/WEB-INF/resources/testFiles/" + id;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            ServletContext context = getServletContext();
            InputStream inp = context.getResourceAsStream(filename);
            Document doc = db.parse(inp);

            // Doc to string
            DOMSource domSource = new DOMSource(doc);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);
            String txt = sw.toString();

            // Construir respuesta
            res.setContentType("text/xml");
            res.setCharacterEncoding("UTF-8");
            writer.print(txt);
            writer.flush();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

// //Leer archivo
// String id = req.getParameter("id");
// String filename = "/WEB-INF/resources/testFiles/"+id;
// ServletContext context = getServletContext();
// String text = "";

// InputStream inp = context.getResourceAsStream(filename);
// PrintWriter writer = res.getWriter();

// if (inp != null) {
// InputStreamReader isr = new InputStreamReader(inp);
// BufferedReader reader = new BufferedReader(isr);
// String lines = reader.lines().collect(Collectors.joining("\n"));

// System.out.println(lines);

// // Construir respuesta
// res.setContentType("text/xml");
// res.setCharacterEncoding("UTF-8");
// writer.print(lines);
// writer.flush();

/*
 * 1- Leer archivo xml java
 * https://www.javatpoint.com/how-to-read-xml-file-in-java
 * 
 * 2- XML to string
 * http://www.java2s.com/Tutorials/Java/XML/
 * How_to_convert_org_w3c_dom_Document_to_String.htm
 */
