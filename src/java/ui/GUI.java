package ui;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private static MainPanel main;

    public static void main(String[] args) {
        initializeGUI();
    }

    public static void initializeGUI() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = (screenSize.height * 0.8);
        double width = screenSize.width * 0.8;

        JFrame frame = new JFrame();
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
