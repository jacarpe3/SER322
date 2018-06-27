package ui;

import database.DBManager;
import javax.swing.*;
import java.awt.*;

/**
 * Main class for the GUI of the application
 * @author Josh Carpenter
 * @version 1.0
 */
public class GUI {

    private static MainPanel main;

    /**
     * Main method to start program
     */
    public static void main(String[] args) {
        DBManager.getInstance().initializeDB();
        DBManager.getInstance().populateDB();
        initializeGUI();
    }

    /**
     * Initializes the frame and adds main panel to it
     */
    public static void initializeGUI() {

        main = new MainPanel();
        JFrame frame = new JFrame();
        frame.add(main);
        frame.setTitle("Comic Book Value Look Up Tool");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getRootPane().setDefaultButton(getMainPanel().getSearchButton());
        frame.setPreferredSize(new Dimension(1250, 700));
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);


    }

    /**
     * Used to pass the main panel object out to other classes
     * @return MainPanel object
     */
    public static MainPanel getMainPanel() {
        return main;
    }

}
