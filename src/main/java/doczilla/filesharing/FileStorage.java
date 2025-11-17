package doczilla.filesharing;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileStorage {
    private static final Map<String, FileStatistic> files = new HashMap<>();

    static {
        File folder = new File("src/main/resources/files");
        for (File file : folder.listFiles()) {
            UUID id = UUID.randomUUID();
            String name = file.getName();
            long size = file.length();
            Date uploadDate = null;
            try {
                uploadDate = Date.from(((FileTime) Files.getAttribute(file.toPath(), "creationTime")).toInstant());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            FileStatistic fileStatistic = new FileStatistic(id, name, size, uploadDate);
            files.put(String.valueOf(id), fileStatistic);
        }
    }

    /**
     * @param uuid id файла
     * @return файл по его id
     */
    public static File getFileById(String uuid) {
        return new File(AppProperties.get("files_path") + files.get(uuid).getName());
    }

    /**
     * @param fileName имя файла
     * @return статистику файла по его имени
     */
    public static FileStatistic getFileStatisticByName(String fileName) {
        return files.values()
                .stream()
                .filter(fileStatistic -> fileStatistic.getName().equals(fileName))
                .findAny()
                .orElse(null);
    }

    /**
     * @param filePart файл, переданный с клиента
     * @return сохраняет статистику по переданному файлу
     */
    public static FileStatistic saveFileStatistic(Part filePart) throws IOException {
        UUID id = UUID.randomUUID();
        String name = filePart.getSubmittedFileName();
        FileStatistic fileStatistic = getFileStatisticByName(filePart.getSubmittedFileName());
        if (fileStatistic != null) {
            id = fileStatistic.getId();
        }
        long size = filePart.getSize();
        Date uploadDate = Date.from(((FileTime) Files.getAttribute(Path.of(AppProperties.get("files_path") + name), "creationTime")).toInstant());
        fileStatistic = new FileStatistic(id, name, size, uploadDate);
        files.put(String.valueOf(id), fileStatistic);
        return fileStatistic;
    }

    /**
     * @return статистику по всем загруженным файлам
     */
    public static Collection<FileStatistic> getAllFileStatistics() {
        return files.values();
    }
}
