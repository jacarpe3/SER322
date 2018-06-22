import database.DBManager;

import java.sql.SQLException;

/**
 * This class if for testing only
 */
public class DriverClass {

    public static void main(String[] args) throws SQLException {

        String t1 = "insert into teams values (13, 'Rays', 'Tampa Bay', 'American');";
        String t2 = "insert into teams values (14, 'Diamondbacks', 'Arizona', 'American');";
        DBManager.getInstance().update(t1);
        DBManager.getInstance().update(t2);

    }

}
