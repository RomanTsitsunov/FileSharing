package doczilla.filesharing;

import doczilla.filesharing.servlets.AuthServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.parseInt(AppProperties.get("app.port")));
        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ctx.setContextPath("/");
        ServletHolder holderHome = new ServletHolder("default", DefaultServlet.class);
        holderHome.setInitParameter("resourceBase", Main.class.getResource("/static").toExternalForm());
        holderHome.setInitParameter("dirAllowed", "false");
        ctx.addServlet(holderHome, "/");

        ctx.addServlet(new ServletHolder(new AuthServlet()), AppProperties.get("login_api"));

        server.setHandler(ctx);
        server.start();
        server.join();
    }
}