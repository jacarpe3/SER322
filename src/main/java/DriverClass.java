import database.DBManager;

import java.awt.image.BufferedImage;
import java.io.File;


/**
 * This class if for testing only
 */
public class DriverClass {

    public static void main(String[] args) throws Exception {

        String t1 = "insert into players values (11, 'Tom Hanks', 'Reds', 'Outfielder', '1965');";
        String t2 = "insert into players values (12, 'Chris Williams', 'Rangers', 'First Base', '1975');";
        File f1 = new File("src/main/resources/test1.gif");
        File f2 = new File("src/main/resources/test2.gif");

        //DBManager.getInstance().modify(t1);
        //DBManager.getInstance().modify(t2);

        //DBManager.getInstance().insertImage(f1, 1);
        //DBManager.getInstance().insertImage(f2, 2);

        BufferedImage imageTest = DBManager.getInstance().retrieveImage(1);

    }

}
