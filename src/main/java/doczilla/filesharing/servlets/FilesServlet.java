package doczilla.filesharing.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import doczilla.filesharing.AppProperties;
import doczilla.filesharing.FileStatistic;
import doczilla.filesharing.FileStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Map;

@MultipartConfig
public class FilesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Collection<FileStatistic> fileStatistics = FileStorage.getAllFileStatistics();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.println(objectMapper.writeValueAsString(Map.of("files", fileStatistics)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Part filePart = req.getPart("file");
        if (filePart != null) {
            InputStream input = filePart.getInputStream();
            Files.copy(input, Path.of(AppProperties.get("files_path") + filePart.getSubmittedFileName()), StandardCopyOption.REPLACE_EXISTING);
            FileStatistic fileStatistic = FileStorage.saveFileStatistic(filePart);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            out.println(objectMapper.writeValueAsString(Map.of("file", fileStatistic)));
        }
    }
}
