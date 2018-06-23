package database;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to connect with and execute SQL queries with PostgreSQL database
 * @author Josh Carpenter
 * @version 1.0
 */
@SuppressWarnings("ConstantConditions")
public class DBManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/ser322";
    private static final String UN = "postgres";
    private static final String PW = "test123";
    private static DBManager db = null;
    private static Connection c;
    private static String statusMsg;

    // Locked constructor for Singleton class
    private DBManager() {}

    /**
     * If the DBManager instance is null it is instantiated and returned
     * @return DBManager instance
     */
    public static synchronized DBManager getInstance() {
        if (db == null) {
            db = new DBManager();
        }
        return db;
    }

    /**
     * @return Status message for success or failure of a procedure/execution
     */
    public static String getStatusMsg() {
        return statusMsg;
    }

    /**
     * Used to establish connection with the PostgreSQL database
     * @return Connection to the PostgreSQL database
     */
    private Connection connect() {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            //ignore
        }
        try {
            c = DriverManager.getConnection(URL, UN, PW);
            statusMsg = "Connected Successfully";
        } catch (SQLException e) {
            statusMsg = "Failed to connect!";
        }
        return c;
    }

    /**
     * Used for modify operations such as CREATE, INSERT, DELETE, and UPDATE
     * @param sqlQuery sql string to execute
     */
    public void modify(String sqlQuery) {
        c = connect();
        try {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sqlQuery);
            stmt.close();
            c.close();
        } catch (SQLException e) {
            statusMsg = "Query failed!";
        }

    }

    /**
     * Used for SELECT operation
     * @return Result List of comic entities matching query
     * @param sqlQuery sql query string to execute
     */
    public List<ComicEntity> query(String sqlQuery) {
        c = connect();
        List<ComicEntity> comicEntityList = new ArrayList<>();
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                ComicEntity comic = new ComicEntity();
                comic.setUPC(rs.getString("UPC"));
                comic.setIssueNum(rs.getInt("issueNum"));
                comic.setPubDate(rs.getDate("pubDate"));
                comic.setPubName(rs.getString("pubName"));
                comic.setIssueTitle(rs.getString("issueTitle"));
                comic.setSeriesName(rs.getString("issueName"));
                comic.setThumbnail(new ImageIcon(ImageIO.read(rs.getBinaryStream("CoverImage"))));
                comicEntityList.add(comic);
            }
            rs.close();
            stmt.close();
            c.close();
            statusMsg = "Search found " + comicEntityList.size() + " results";
        } catch (SQLException | IOException e) {
            statusMsg = "Query failed!";
        }
        return comicEntityList;
    }

    /**
     * Used to insert file into PostgreSQL database
     * @param file the image file to insert
     * @param coverID the coverID of the image
     */
    public void insertImage(File file, int coverID) {
        c = connect();
        try {
            FileInputStream stream = new FileInputStream(file);
            PreparedStatement ps = c.prepareStatement("INSERT INTO covers VALUES (?, ?)");
            ps.setInt(1, coverID);
            ps.setBinaryStream(2, stream, file.length());
            ps.executeUpdate();
            ps.close();
            stream.close();
            c.close();
        } catch (SQLException | IOException e) {
            statusMsg = "Error inserting image!";
        }

    }

    /**
     * Used to retrieve an image from PostgreSQL database
     * @param coverID the coverID to retrieve
     * @return BufferedImage object of the image
     */
    public BufferedImage retrieveImage(int coverID) {
        c = connect();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT coverImage FROM covers WHERE coverID = ?");
            ps.setInt(1, coverID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                InputStream stream = rs.getBinaryStream(1);
                return ImageIO.read(stream);
            }
        } catch (SQLException | IOException e) {
            statusMsg = "Error retrieving image!";
        }
        statusMsg = "No image found!";
        return null;
    }

}
