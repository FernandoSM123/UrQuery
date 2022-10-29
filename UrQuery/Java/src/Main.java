import java.io.File;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.descriptor.web.ErrorPage;

public class Main{
   public static void main(String[] args) throws Exception {

       String webappsDirLocation = "";
       int port = 8001;

       try{
        if (args.length > 0 ){
            port = Integer.valueOf(args[0]);
        }
    } catch (Exception e){
        System.out.format("Invalid port number '%s' %n", args[0]);
        System.exit(-1);
    }
    //Establecer puerto
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);

    // Init App Context
    StandardContext ctx = (StandardContext) tomcat.addWebapp("",new File(webappsDirLocation).getAbsolutePath());
    WebResourceRoot root = new StandardRoot(ctx);


    // Map pages(index) 
    var pages = new File("./pages");
    root.addPreResources(new DirResourceSet(root, "/",
    pages.getAbsolutePath(), "/"));
    ctx.setResources(root);


    // Map WEB-INF/classes 
    var classes = new File("./classes");
    root.addPreResources(new DirResourceSet(root, "/WEB-INF/classes",
    classes.getAbsolutePath(), "/"));
    ctx.setResources(root);

     // Map WEB-INF/resources 
    var resources = new File("./resources");
    root.addPreResources(new DirResourceSet(root, "/WEB-INF/resources",
    resources.getAbsolutePath(), "/"));
    ctx.setResources(root);

    //Iniciar servidor
    tomcat.start();
    tomcat.getServer().await();
}
}