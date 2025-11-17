package doczilla.filesharing;

import java.util.Date;
import java.util.UUID;

public class FileStatistic {
    private final UUID id;
    private final String name;
    private final long size;
    private final Date uploadDate;
    private Date lastDownloadDate;
    private final String downloadLink;
    private int downloadCount;

    public FileStatistic(UUID id, String name, long size, Date uploadDate) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.uploadDate = uploadDate;
        lastDownloadDate = null;
        this.downloadLink = "/download/" + id;
        this.downloadCount = 0;
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

    public Date getLastDownloadDate() {
        return lastDownloadDate;
    }

    public void setLastDownloadDate(Date lastDownloadDate) {
        this.lastDownloadDate = lastDownloadDate;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }
}
