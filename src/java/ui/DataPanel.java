package ui;

import database.ComicEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

public class DataPanel extends JPanel {

    private final String[] columns = {"Thumbnail", "Series UPC", "Series Name", "Issue Title",
                                                "Writer", "Artist", "Publisher", "Publish Date", "Value"};

	public DataPanel() {

        DefaultTableModel model = new DefaultTableModel(getResultsData(), columns);

	    JTable table = new JTable();
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, scrollPane);

	}

	private Object[][] getResultsData() {
	    List<ComicEntity> list = GUI.getMainPanel().getResultsList();
        Object[][] data = new Object[list.size()][columns.length];
	    for (int i = 0; i < list.size(); i++) {
	        for (int j = 0; j < columns.length; j++) {
	            data[i][j] = list.get(i).getThumbnail();
                data[i][j] = list.get(i).getUPC();
                data[i][j] = list.get(i).getSeriesName();
                data[i][j] = list.get(i).getIssueTitle();
                data[i][j] = list.get(i).getWriter();
                data[i][j] = list.get(i).getArtist();
                data[i][j] = list.get(i).getPubName();
                data[i][j] = list.get(i).getPubDate();
                data[i][j] = list.get(i).getValue();
            }
        }
        return data;
    }
}
