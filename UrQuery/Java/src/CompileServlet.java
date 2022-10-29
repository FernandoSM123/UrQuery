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

import java.net.http.*;
import java.net.http.HttpResponse.*;
import java.net.http.HttpRequest.*;

@WebServlet(
    name = "CompileServlet",
    urlPatterns = {"/compile"}
    )

public class CompileServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws IOException {

        String EA = req.getParameter("EA");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String strTimeStamp=timestamp.toString();
        EA = strTimeStamp + "\n" + EA;
        PrintWriter writer = res.getWriter();

        //Prolog request
        
        // var PORT = 8002;
        // var API = "/test";
        // var SERVICE = String.format("http://localhost:%d%s", PORT, API); 
    
        // var client = HttpClient.newHttpClient();
        // var uri = URI.create(SERVICE);

        // var request = HttpRequest.newBuilder(uri)
        // .version(HttpClient.Version.HTTP_1_1)
        // .header("Content-Type", "text/plain")
        // .header("accept", "text/plain")
        // //.POST(BodyPublishers.ofString(body))
        // .build();

        // var response = client.send(request, BodyHandlers.ofString());
        // System.out.println("*** Response ***");
        // System.out.println(response.body());

        // Construir respuesta
        res.setContentType("text/html");
        res.setCharacterEncoding("UTF-8");
        writer.print(EA);
        writer.flush();

    }
}