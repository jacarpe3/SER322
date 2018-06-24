package database;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used to connect with and execute SQL queries with PostgreSQL database
 * @author Josh Carpenter
 * @version 1.0
 */
@SuppressWarnings("ConstantConditions")
public class DBManager {

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
    public void initializeDB() {
        c = connect(SQL.Database.PS_URL);
        executeUpdateQuery(SQL.Drop.DB);
        executeUpdateQuery(SQL.Create.DB);
        closeConnection();
        statusMsg = "Database initialized";
    }

    /**
     * Populates the database with data
     */
    public void populateDB() {
        statusMsg = "Populating database...";
        getInstance().modify(
                SQL.Create.EXTENSION,
                SQL.Create.TABLE_PUBLISHER,
                SQL.Create.TABLE_CONTRIBUTOR,
                SQL.Create.TABLE_COVERS,
                SQL.Create.TABLE_SERIES,
                SQL.Create.TABLE_COMICS,
                SQL.Create.TABLE_COMIC_WRITERS,
                SQL.Create.TABLE_ARTIST_ROLES,
                SQL.Create.TABLE_COMIC_ARTISTS,
                SQL.Create.TABLE_COMIC_COVERS,

                SQL.Insert.PUBLISHER,
                SQL.Insert.CONTRIBUTOR,
                SQL.Insert.ARTIST_ROLES,
                SQL.Insert.SERIES
        );

//        for (int i = 1; i <= 30; i++) {
//            updateCoverImage(i);
//        }

        statusMsg = "Database ready";
    }

    /**
     * Used for SELECT operation
     * @return Result List of comic entities matching query
     * @param params Given search criteria in the text fields
     */
    public List<ComicEntity> query(Map<String, String> params) {
        c = connect(SQL.Database.DB_URL);
        List<ComicEntity> comicEntityList = new ArrayList<>();
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(buildQuery(params));
            while (rs.next()) {
                ComicEntity comic = new ComicEntity();
                comic.setUPC(rs.getString(SQL.Columns.SERIES_UPC));
                comic.setIssueNum(rs.getInt(SQL.Columns.ISSUE_NUM));
                comic.setPubDate(rs.getDate(SQL.Columns.PUB_DATE));
                comic.setPubName(rs.getString(SQL.Columns.PUB_NAME));
                comic.setIssueTitle(rs.getString(SQL.Columns.ISSUE_TITLE));
                comic.setSeriesName(rs.getString(SQL.Columns.SERIES_NAME));
                comic.setThumbnail(new ImageIcon(ImageIO.read(rs.getBinaryStream(SQL.Columns.THUMB_IMAGE))));
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
     * Used for modify operations such as CREATE, INSERT, DELETE, and UPDATE
     * @param sqlQuery sql string to execute
     */
    private void modify(String... sqlQuery) {
        c = connect(SQL.Database.DB_URL);
        executeUpdateQuery(sqlQuery);
        closeConnection();
    }

    /**
     * Builds the search query based on which parameters are given
     * Search parameters MUST be passed in this order: {seriesUPC, fName, lName, series, title, pub}
     * @param params variable arguments String array containing search parameters
     * @return String object representing the SQL query
     */
    private static String buildQuery(Map<String, String> params) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ser322comics WHERE ");

        int count = 0;
        for (String param : params.keySet()) {
            count++;
            if (!params.get(param).isEmpty()) {
                String thisParam = param + " = '" + params.get(param) + "'";
                sb.append(thisParam);
            }
            if (count < params.size()) {
                sb.append(" AND ");
            } else {
                sb.append(" ORDER BY ").append(SQL.Columns.ISSUE_TITLE).append(" ASC;");
            }
        }
        return sb.toString();
    }

    /**
     * Used to update cover images in covers table
     * Assumes file name matches the coverID
     * @param coverID coverID to update
     */
    private void updateCoverImage(int coverID) {
        c = connect(SQL.Database.DB_URL);
        String file = "/images/" + coverID + ".gif";
        try {
            InputStream stream = getClass().getResourceAsStream(file);
            PreparedStatement ps = c.prepareStatement(SQL.Update.THUMBNAIL_IMAGE);
            ps.setBinaryStream(1, stream, file.length());
            ps.setInt(2, coverID);
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
            c = DriverManager.getConnection(url, SQL.Database.UN, SQL.Database.PW);
        } catch (SQLException e) {
            statusMsg = "Failed to connect!";
        }
        return c;
    }

    /**
     * Private supporting method for executing queries
     * Connection object must be active prior to its use (and closed after)
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
