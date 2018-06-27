package database;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
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
     * Creates the database
     */
    public void initializeDB() {
        c = connect(SQL.Database.PS_URL);
        executeUpdateQuery(SQL.Database.DROP);
        executeUpdateQuery(SQL.Create.DB);
        closeConnection();
    }

    /**
     * Populates the database with data
     */
    public void populateDB() {
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
                SQL.Create.VIEW_COMIC_LISTING,
                SQL.Insert.PUBLISHER,
                SQL.Insert.CONTRIBUTOR,
                SQL.Insert.SERIES,
                SQL.Insert.COMICS,
                SQL.Insert.ARTIST_ROLES,
                SQL.Insert.COMIC_WRITERS,
                SQL.Insert.COVERS,
                SQL.Insert.COMIC_COVERS,
                SQL.Insert.COMIC_ARTISTS
        );

        for (int i = 1; i <= 30; i++) {
            updateImage(i);
        }

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
                comic.setComicID(rs.getInt(SQL.Columns.COMIC_ID));
                comic.setSeriesNo(rs.getString(SQL.Columns.SERIES_NO));
                comic.setIssueNum(rs.getString(SQL.Columns.ISSUE_NUM));
                comic.setPubDate(rs.getObject(SQL.Columns.PUB_DATE, LocalDate.class));
                comic.setPubName(rs.getString(SQL.Columns.PUB_NAME));
                comic.setIssueTitle(rs.getString(SQL.Columns.ISSUE_TITLE));
                comic.setSeriesName(rs.getString(SQL.Columns.SERIES_NAME));
                comic.setValue(rs.getDouble(SQL.Columns.VALUE));
                comic.setWriter(rs.getString(SQL.Columns.WRITERS));
                comic.setArtist(rs.getString(SQL.Columns.ARTISTS));
                comic.setThumbnail(new ImageIcon(ImageIO.read(rs.getBinaryStream(SQL.Columns.THUMB_IMAGE))));
                comicEntityList.add(comic);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (SQLException | IOException e) {
            //Ignore
        }
        return comicEntityList;
    }

    /**
     * Used to retrieve the cover from ComicCovers table in the DELETE function in DataPanel
      * @param comic the comic to look for
     * @return the cover int
     */
    public void deleteRow(int comic) {
        c = connect(SQL.Database.DB_URL);
        int cover = 0;
        try {
            PreparedStatement ps = c.prepareStatement(SQL.Select.COVER);
            ps.setInt(1, comic);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cover = rs.getInt(SQL.Columns.COVER);
            }
            ps = c.prepareStatement(SQL.Delete.ComicWriters.COMIC);
            ps.setInt(1, comic);
            ps.executeUpdate();
            ps = c.prepareStatement(SQL.Delete.ComicArtists.COMIC);
            ps.setInt(1, comic);
            ps.executeUpdate();
            ps = c.prepareStatement(SQL.Delete.ComicCovers.COMIC);
            ps.setInt(1, comic);
            ps.executeUpdate();
            ps = c.prepareStatement(SQL.Delete.Comics.COMIC_ID);
            ps.setInt(1, comic);
            ps.executeUpdate();
            ps = c.prepareStatement(SQL.Delete.Covers.COVER_ID);
            ps.setInt(1, cover);
            ps.executeUpdate();
            ps.close();
            c.close();
        } catch (SQLException e) {
            e.getMessage();
        }

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
     * Uses 'LIKE' to return partial matches
     * @param params variable arguments String array containing search parameters
     * @return String object representing the SQL query
     */
    private static String buildQuery(Map<String, String> params) {

        StringBuilder sb = new StringBuilder("SELECT * FROM fullComicListing WHERE ");

        if (params.keySet().contains(SQL.Select.ALL.getClass().getSimpleName())) {
            return SQL.Select.ALL;
        }

        int count = 0;
        for (String param : params.keySet()) {
            count++;
            if (!params.get(param).isEmpty()) {
                String thisParam = "upper(" + param + ") LIKE '%" + params.get(param).toUpperCase() + "%'";
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
     * Used to update thumbnail images in covers table
     * Assumes file name matches the coverID
     * @param coverID coverID to update
     */
    public void updateImage(int coverID) {
        c = connect(SQL.Database.DB_URL);
        String fileLoc = "/images/" + coverID + ".jpg";
        try {
            InputStream stream = getClass().getResourceAsStream(fileLoc);
            PreparedStatement ps = c.prepareStatement(SQL.Update.THUMBNAIL_IMAGE);
            ps.setBinaryStream(1, stream);
            ps.setInt(2, coverID);
            ps.executeUpdate();
            ps.close();
            stream.close();
            c.close();
        } catch (SQLException | IOException e) {
            //Ignore
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
            //Ignore
        }
        try {
            c = DriverManager.getConnection(url, SQL.Database.UN, SQL.Database.PW);
        } catch (SQLException e) {
            if (e.getMessage().contains("password authentication failed")) {
                String msg = "<html>PostgreSQL superuser 'postgres' password is incorrect!<br/>" +
                        "Please login and run the following command:<br/><br/>" +
                        "ALTER USER postgres WITH PASSWORD 'test123';<html>";
                JLabel lbl = new JLabel(msg, JLabel.CENTER);
                JOptionPane.showConfirmDialog(null, lbl, "Authentication Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
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
            //Ignore
        }
    }

    /**
     * Closes the open database connection
     */
    private void closeConnection() {
        try {
            c.close();
        } catch (SQLException e) {
            //Ignore
        }
    }

}
