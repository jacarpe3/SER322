package ui;
import java.awt.*;
import javax.swing.*;
public class DataTable extends JFrame {
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
	public static void main(String[] args) {
		DataTable gui = new DataTable();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(600,200);
		gui.setVisible(true);
		gui.setTitle("Demo");
	}
}
