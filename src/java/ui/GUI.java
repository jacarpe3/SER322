package ui;

import database.DBManager;
import javax.swing.*;
import java.awt.*;

public class GUI {

    private static MainPanel main;

    public static void main(String[] args) {
        DBManager.getInstance().initializeDB();
        DBManager.getInstance().populateDB();
        initializeGUI();
    }

    public static void initializeGUI() {

        JFrame frame = new JFrame();
        main = new MainPanel();
        frame.add(main);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1250, 700));
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);


    }

    public static MainPanel getMainPanel() {
        return main;
    }

}
