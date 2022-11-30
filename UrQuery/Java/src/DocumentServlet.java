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

import java.util.Arrays;

import com.google.gson.*;

import java.io.FileWriter;
import java.io.BufferedWriter;

//Servlet para subir o traer un archivo xml

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


    //Guardar archivo xml
    public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws IOException {

        try {
            //Sacar valores de la request
            String body = req.getReader().lines().collect(Collectors.joining());
            JsonObject json = new Gson().fromJson(body, JsonObject.class);
            String file = json.get("fileTxt").getAsString();
            String fileName = json.get("fileName").getAsString();

            ServletContext cxt = getServletContext();
            String path = cxt.getRealPath("WEB-INF/resources/testFiles/") + fileName + ".xml";

            PrintWriter writer = res.getWriter();

            //Escribir en archivo
            FileWriter fw = new FileWriter(path,false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(file);
            bw.close();

             // Construir respuesta
            res.setContentType("txt/xml");
            res.setCharacterEncoding("UTF-8");
            writer.print(fileName);
            writer.flush();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
