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

    private static final String psURL = "jdbc:postgresql://localhost:5432/";
    private static final String dbURL = psURL + "ser322comics";
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
     * Creates the database
     */
    public void initiateDB() {
        c = connect(psURL);
        executeUpdateQuery(SQL.Create.db);
        closeConnection();
        statusMsg = "Database initialized";
    }

    /**
     * Populates the database with data
     */
    public void populateDB() {
        getInstance().modify(
                SQL.Create.extension,
                SQL.Create.tablePublisher,
                SQL.Create.tableContributor,
                SQL.Create.tableCovers,
                SQL.Create.tableSeries,
                SQL.Create.tableComics,
                SQL.Create.tableComicWriters,
                SQL.Create.tableArtistRoles,
                SQL.Create.tableComicArtists,
                SQL.Create.tableComicCovers,

                SQL.Insert.publisher,
                SQL.Insert.contributor,
                SQL.Insert.artistRoles,
                SQL.Insert.series
        );

        statusMsg = "Database ready";
    }

    /**
     * Used for modify operations such as CREATE, INSERT, DELETE, and UPDATE
     * @param sqlQuery sql string to execute
     */
    public void modify(String... sqlQuery) {
        c = connect(dbURL);
        executeUpdateQuery(sqlQuery);
        closeConnection();
    }

    /**
     * Used for SELECT operation
     * @return Result List of comic entities matching query
     * @param sqlQuery sql query string to execute
     */
    public List<ComicEntity> query(String sqlQuery) {
        c = connect(dbURL);
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
                comic.setThumbnail(new ImageIcon(ImageIO.read(rs.getBinaryStream("thumbnailImage"))));
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
     * Used to insert cover with image into PostgreSQL database
     * @param file the image file to insert
     */
    private void insertCover(File file) {
        c = connect(dbURL);
        try {
            FileInputStream stream = new FileInputStream(file);
            PreparedStatement ps = c.prepareStatement("INSERT INTO covers VALUES (?, ?)");
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
     * Used to establish connection with the PostgreSQL database
     * @return Connection to the PostgreSQL database
     */
    private static Connection connect(String url) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            //ignore
        }
        try {
            c = DriverManager.getConnection(url, UN, PW);
            statusMsg = "Connected Successfully";
        } catch (SQLException e) {
            statusMsg = "Failed to connect!";
        }
        return c;
    }

    /**
     * Private supporting method for executing queries
     * @param sqlQuery one or more SQL queries to be executed
     */
    private void executeUpdateQuery(String... sqlQuery) {
        try {
            Statement stmt = c.createStatement();
            for (int i = 0; i < sqlQuery.length; i++) {
                stmt.executeUpdate(sqlQuery[i]);
            }
            stmt.close();
        } catch (SQLException e) {
            statusMsg = "Query failed!";
        }
    }

    /**
     * Closes the open database connection
     */
    private void closeConnection() {
        try {
            c.close();
        } catch (SQLException e) {
            statusMsg = "Unable to close connection!";
        }
    }

}
