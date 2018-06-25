package database;

import javax.swing.ImageIcon;
import java.time.LocalDate;

/**
 * Represents a comic entity and contains all attributes as variables.
 * Attributes can only be set during instantiation.
 * @author Josh Carpenter
 * @version 1.0
 */
public class ComicEntity {

    private double value;
    private LocalDate pubDate;
    private String UPC;
    private String pubName;
    private String issueTitle;
    private String seriesName;
    private String writer;
    private String artist;
    private String issueNum;
    private ImageIcon cover;
    private ImageIcon thumbnail;

    public String getIssueNum() {
        return issueNum;
    }

    public void setIssueNum(String issueNum) {
        this.issueNum = issueNum;
    }

    public LocalDate getPubDate() {
        return pubDate;
    }

    public void setPubDate(LocalDate pubDate) {
        this.pubDate = pubDate;
    }

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
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

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public ImageIcon getCover() {
        return cover;
    }

    public void setCover(ImageIcon cover) {
        this.cover = cover;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
