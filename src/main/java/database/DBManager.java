package database;

import java.sql.*;

@SuppressWarnings("ConstantConditions")
public class DBManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/ser322";
    private static final String UN = "postgres";
    private static final String PW = "test123";
    private static DBManager db = null;
    private static Connection c;

    private DBManager() {}

    public static DBManager getInstance() {
        if (db == null) {
            db = new DBManager();
        }
        return db;
    }

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

    public void update(String sqlQuery) throws SQLException {
        c = connect();
        Statement stmt = c.createStatement();
        stmt.executeUpdate(sqlQuery);
        stmt.close();
        c.close();
    }

    public void query(String sqlQuery) throws SQLException {
        c = connect();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery);
        while (rs.next()) {
            //TODO implement
        }
        rs.close();
        stmt.close();
        c.close();
    }

}
