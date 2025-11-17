package doczilla.filesharing;

import java.util.Date;
import java.util.UUID;

public class FileStatistic {
    private final UUID id;
    private final String name;
    private final long size;
    private final Date uploadDate;
    private final String downloadLink;

    public FileStatistic(UUID id, String name, long size, Date uploadDate) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.uploadDate = uploadDate;
        this.downloadLink = "/download/" + id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}
