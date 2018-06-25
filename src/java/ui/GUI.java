package ui;

import database.DBManager;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private static MainPanel main;
    private static JFrame frame;

    public static void main(String[] args) {
        DBManager.getInstance().initializeDB();
        DBManager.getInstance().populateDB();
        initializeGUI();
    }

    public static void initializeGUI() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = (screenSize.height * 0.65);
        double width = screenSize.width * 0.65;

        frame = new JFrame();
        main = new MainPanel();
        frame.add(main);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension((int)width, (int)height));
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);


    }

    public static MainPanel getMainPanel() {
        return main;
    }

}
