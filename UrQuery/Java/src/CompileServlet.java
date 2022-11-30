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

import java.util.HashMap;

import java.util.stream.*;

import com.google.gson.*;

//Servlet para compilar script de Urquery a js

@WebServlet(name = "CompileServlet", urlPatterns = { "/compile" })

public class CompileServlet extends HttpServlet {

    // Hacer request al servidor de prolog
    public HttpResponse<String> prologRequest(String url, String urQueryScript) throws Exception, InterruptedException {

        String requestBody = "{" + '"' + "urQuery" + '"' + ":" + '"' + urQueryScript +'"' + "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client.send(request,HttpResponse.BodyHandlers.ofString());
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        try {
            PrintWriter writer = res.getWriter();

            //Sacar valores de la request
            String body = req.getReader().lines().collect(Collectors.joining());
            JsonObject json = new Gson().fromJson(body, JsonObject.class);
            String script = json.get("script").getAsString();
            System.out.println(script);

            // Prolog request
            //HttpResponse<String> result = prologRequest("http://localhost:8082/compile",EA);
            HttpResponse<String> result = prologRequest("http://localhost:8082/compile",script);

            System.out.println(result.body());
            System.out.println("*** Prolog response ***");
            System.out.println(result.body());

            // Construir respuesta
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            writer.print(result.body());
            writer.flush();

        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}