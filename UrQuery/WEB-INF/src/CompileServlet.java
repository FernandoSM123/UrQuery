package com.UrQuery.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

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

             // Construir respuesta
            PrintWriter writer = res.getWriter();
            writer.print(EA);
            writer.flush();
     
    }
}