import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Timestamp;

public class CompileServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {


            res.setContentType("text/html");
            res.setCharacterEncoding("UTF-8");
            String EA = req.getParameter("EA");
            PrintWriter pw = res.getWriter();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String strTimeStamp=timestamp.toString();
            EA = strTimeStamp + "\n" + EA;
            
            //Prolog request
            String GET_URL = "http://localhost:8082/test";
            URL obj = new URL(GET_URL);
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Content-Type", "text/html");
		    con.setRequestMethod("GET");

            // Construir respuesta
            PrintWriter writer = res.getWriter();
            writer.print(EA);
            writer.flush();
     
    }
}