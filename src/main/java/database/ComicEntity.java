package database;

import javax.swing.ImageIcon;
import java.sql.Date;

/**
 * Represents a comic entity and contains all attributes as variables.
 * Attributes can only be set during instantiation.
 * @author Josh Carpenter
 * @version 1.0
 */
public class ComicEntity {

    private int issueNum;
    private Date pubDate;
    private String ISBN;
    private String pubName;
    private String issueTitle;
    private String seriesName;
    private ImageIcon thumbnail;

    public int getIssueNum() {
        return issueNum;
    }

    public void setIssueNum(int issueNum) {
        this.issueNum = issueNum;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public ImageIcon getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageIcon thumbnail) {
        this.thumbnail = thumbnail;
    }
}
