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
    public void initializeDB() {
        c = connect(psURL);
        executeUpdateQuery(SQL.Drop.db);
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
        c = connect(dbURL);
        List<ComicEntity> comicEntityList = new ArrayList<>();
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(buildQuery(params));
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
     * Used for modify operations such as CREATE, INSERT, DELETE, and UPDATE
     * @param sqlQuery sql string to execute
     */
    private void modify(String... sqlQuery) {
        c = connect(dbURL);
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
                sb.append(" ORDER BY ").append(Constants.Search.issueTitle).append(" ASC;");
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
        c = connect(dbURL);
        String path = "src/main/resources/thumb";
        String ext = ".gif";
        File file = new File(path + coverID + ext);
        try {
            FileInputStream stream = new FileInputStream(file);
            PreparedStatement ps = c.prepareStatement(SQL.Update.thumbnailImage);
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
            c = DriverManager.getConnection(url, UN, PW);
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
