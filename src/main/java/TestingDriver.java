import database.DBManager;
import database.SQL;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * This class if for testing only
 */
public class TestingDriver {

    public static void main(String[] args) throws Exception {

        DBManager.getInstance().initiateDB();
        DBManager.getInstance().populateDB();

        String t1 = "insert into players values (11, 'Tom Hanks', 'Reds', 'Outfielder', '1965');";
        String t2 = "insert into players values (12, 'Chris Williams', 'Rangers', 'First Base', '1975');";
        File f1 = new File("src/main/resources/thumb1.gif");
        File f2 = new File("src/main/resources/thumb2.gif");

        //DBManager.getInstance().modify(t1);
        //DBManager.getInstance().modify(t2);

        //DBManager.getInstance().insertImage(f1, 1);
        //DBManager.getInstance().insertImage(f2, 2);
        //DisplayImage.displayGUI("thumb1.gif");

//        BufferedImage imageTest1 = DBManager.getInstance().retrieveImage(1);
//        BufferedImage imageTest2 = DBManager.getInstance().retrieveImage(2);
//        DisplayImage.displayGUI(new ImageIcon(imageTest1));

        DataTable gui = new DataTable();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(600,200);
        gui.setVisible(true);
        gui.setTitle("Demo");

    }

    public static class DisplayImage extends JFrame {

        private ImageIcon img;
        private JLabel lbl;

        public DisplayImage(String fileName) {
            setLayout(new FlowLayout());
            img = new ImageIcon(getClass().getResource(fileName));
            lbl = new JLabel(img);
            add(lbl);
        }

        public DisplayImage(ImageIcon imageIcon) {
            setLayout(new FlowLayout());
            img = imageIcon;
            lbl = new JLabel(img);
            add(lbl);
        }

        public static void displayGUI(String fileName) {
            DisplayImage gui = new DisplayImage(fileName);
            setupGUI(gui);
        }

        public static void displayGUI(ImageIcon imageIcon) {
            DisplayImage gui = new DisplayImage(imageIcon);
            setupGUI(gui);
        }

        private static void setupGUI(DisplayImage gui) {
            gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            gui.setVisible(true);
            gui.setLocation(500, 500);
            gui.pack();
            gui.setTitle("Image Testing");
        }

    }

    public static class DataTable extends JFrame {
        private JTable table;
        private JScrollPane scrollPane;
        public DataTable() {
            String[] columnNames = {"Name", "Gender"};
            Object[][] data = {
                    {"SuperMan","Male"},
                    {"BatMan","Male"},
                    {"Megyn Kelly","Female"}
            };
            table = new JTable(data,columnNames);
            table.setPreferredScrollableViewportSize(new Dimension(500,50));
            table.setFillsViewportHeight(true);

            scrollPane = new JScrollPane(table);
            add(scrollPane);
        }

    }

}
