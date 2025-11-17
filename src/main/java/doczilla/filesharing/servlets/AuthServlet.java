package doczilla.filesharing.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import doczilla.filesharing.User;
import doczilla.filesharing.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> json = objectMapper.readValue(body, Map.class);
        User user = UserDao.loadUserByLoginAndPass(json.get("login"), json.get("pass"));
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.println(objectMapper.createObjectNode().put("login", user.getLogin()));
        }
    }
}
