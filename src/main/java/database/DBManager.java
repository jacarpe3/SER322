package database;

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

    // Locked constructor for Singleton class
    private DBManager() {}

    /**
     * If the DBManager instance is null it is instantiated and returned
     * @return DBManager instance
     */
    public static DBManager getInstance() {
        if (db == null) {
            db = new DBManager();
        }
        return db;
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
            System.out.println("Class not found " + e);
        }
        try {
            c = DriverManager.getConnection(URL, UN, PW);
            System.out.println("Connected Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }

    /**
     * Used for modify operations such as CREATE, INSERT, DELETE, and UPDATE
     * @param sqlQuery zero or more sql strings to execute
     * @throws SQLException during statement creation and execution
     */
    public void modify(String... sqlQuery) throws SQLException {
        c = connect();
        Statement stmt = c.createStatement();
        for (int i = 0; i < sqlQuery.length; i++) {
            stmt.executeUpdate(sqlQuery[i]);
        }
        stmt.close();
        c.close();
    }

    /**
     * Used for SELECT operation
     * @param sqlQuery sql string to execute
     * @throws SQLException during statement creation and execution
     */
    public List<ComicEntity> query(String sqlQuery) throws SQLException {
        c = connect();
        Statement stmt = c.createStatement();
        List<ComicEntity> comicEntityList = new ArrayList<>();
        ResultSet rs = stmt.executeQuery(sqlQuery);
        while (rs.next()) {
            ComicEntity comic = new ComicEntity();
            comic.setISBN(rs.getString("ISBN"));
            comic.setIssueNum(rs.getInt("issueNum"));
            comic.setPubDate(rs.getDate("pubDate"));
            comic.setPubName(rs.getString("pubName"));
            comic.setIssueTitle(rs.getString("issueTitle"));
            comic.setSeriesName(rs.getString("issueName"));
            comicEntityList.add(comic);
        }
        rs.close();
        stmt.close();
        c.close();
        return comicEntityList;
    }

}
