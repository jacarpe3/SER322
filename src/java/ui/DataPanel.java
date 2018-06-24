package ui;

import javax.swing.JPanel;
import javax.swing.JTable;

public class DataPanel extends JPanel {
	public DataPanel() {
		String[] columns = {"Thumbnail", "ISBN", "Issue Title", "Series Name",
				"Writer", "Artist", "Publisher", "Publish Date", "Value"};
		Object[][] data = new Object[5][9];
		JTable table = new JTable(data, columns);
		this.add(table);
	}
}
