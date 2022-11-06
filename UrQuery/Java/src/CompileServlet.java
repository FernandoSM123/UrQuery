import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletContext;

import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Timestamp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//Servlet para compilar script a Urquery a js

@WebServlet(name = "CompileServlet", urlPatterns = { "/compile" })

public class CompileServlet extends HttpServlet {

    // Hacer request al servidor de prolog
    public void prologRequest(String url) throws Exception, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println();
        System.out.println("*** Respuesta de prolog server ***");
        System.out.println(response.body());
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String EA = req.getParameter("EA");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String strTimeStamp = timestamp.toString();
        EA = strTimeStamp + "\n" + EA;
        PrintWriter writer = res.getWriter();

        // Prolog request
        try {
            prologRequest("http://localhost:8082/");
        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }

        // Construir respuesta
        res.setContentType("text/html");
        res.setCharacterEncoding("UTF-8");
        writer.print(EA);
        writer.flush();

    }
}

/*
 * 1-HACER PETICION HTTP JAVA
 * https://zetcode.com/java/getpostrequest/
 */