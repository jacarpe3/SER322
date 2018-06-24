package ui;

import java.awt.*;
import javax.swing.*;

public class MainPanel extends JPanel {
	JLabel head = new JLabel("Comic Book Value Look Up Tool");
	InputPanel input = new InputPanel();
	DataPanel info = new DataPanel();
	JButton search = new JButton("Search");
	JLabel message = new JLabel("Waiting for new Message");
	public MainPanel() {
		head.setFont(new Font("Dialog",1,40));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(head);
		this.add(input);
		this.add(search);
		this.add(info);
		this.add(message);
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		MainPanel mainPanel = new MainPanel();
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(900,900);
		frame.setResizable(false);
	}
}
