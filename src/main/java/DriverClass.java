import database.DBManager;

import java.sql.SQLException;

/**
 * This class if for testing only
 */
public class DriverClass {

    public static void main(String[] args) throws SQLException {

        String t1 = "insert into players values (11, 'Tom Hanks', 'Reds', 'Outfielder', '1965');";
        String t2 = "insert into teams values (12, 'Chris Williams', 'Rangers', 'First Base', '1975');";
        DBManager.getInstance().modify(t1);
        DBManager.getInstance().modify(t2);

    }

}
