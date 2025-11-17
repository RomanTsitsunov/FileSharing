package doczilla.filesharing.servlets;

import doczilla.filesharing.FileStorage;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String uuid = path.substring(1);
        File file = FileStorage.getFileById(uuid);
        String fileName = file.getName();
        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String contentDisposition = String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s", fileName, encoded);
        resp.setHeader("Content-disposition", contentDisposition);
        resp.setContentType("application/octet-stream");
        resp.setContentLengthLong(file.length());
        try (InputStream inputStream = new FileInputStream(file)) {
            resp.getOutputStream().write(inputStream.readAllBytes());
        }
    }
}
