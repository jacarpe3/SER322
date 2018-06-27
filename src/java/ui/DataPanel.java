package ui;

import database.ComicEntity;
import database.DBManager;
import database.SQL;
import ui.RenderData.NumberRenderer;
import ui.RenderData.TextTableRenderer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the DataPanel for results display
 * @author Josh Carpenter
 * @version 1.0
 */
public class DataPanel extends JPanel {

    private JTable table;
    private final String[] columns = {"Cover", "Series #", "Series Name", "Issue Title", "Issue #",
                                                "Writer(s)", "Artist(s)", "Publisher", "Publish Date", "Value", "CoverID"};

    /**
     * Constructor
     */
	public DataPanel() {

        DefaultTableModel model = new NonEditableModel(getAllData(), columns);
        table = new JTable(model);
        formatTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 900));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, scrollPane);
	}

    /**
     * used in Listener for Search button to refresh the table display
     */
	public void refresh(Object[][] data) {
	    table.setModel(new NonEditableModel(data, columns));
	    formatTable(table);
	    GUI.getMainPanel().setMessage(data.length + " search results", Color.BLACK);
    }

    /**
     * Deletes selected rows from the table
     * @return number of rows deleted
     */
    public int deleteSelected() {
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            int comicID = (Integer) table.getModel().getValueAt(i, 10);
            DBManager.getInstance().deleteRow(comicID);
        }
        return rows.length;
    }

    /**
     * sets the JTable object with the correct formatting
     * @param table JTable you wish to format
     */
    private void formatTable(JTable table) {
        table.setPreferredScrollableViewportSize(new Dimension(900, 900));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(ImageIcon.class));
        table.getColumnModel().getColumn(9).setCellRenderer(NumberRenderer.getCurrencyRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(91);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(236);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(125);
        table.getColumnModel().getColumn(6).setPreferredWidth(125);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        table.getColumnModel().getColumn(8).setPreferredWidth(120);
        table.getColumnModel().getColumn(9).setPreferredWidth(65);
        table.removeColumn(table.getColumnModel().getColumn(10));
        table.setRowHeight(140);

        for (int i = 1; i <= 8; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new TextTableRenderer(table));
        }
    }

    public Object[][] getAllData() {
        Map<String, String> params = new HashMap<>();
        params.put(SQL.Select.ALL.getClass().getSimpleName(), "");
        List<ComicEntity> list = DBManager.getInstance().query(params);
        return getObjectArray(list);
    }

    /**
     * Supporting method that converts the List of Comic Entities into a 2-D Object array
     * @return Object[][] containing the results to display in the JTable
     */
	public Object[][] getResultsData() {
	    List<ComicEntity> list = GUI.getMainPanel().getResultsList();
        return getObjectArray(list);
    }

    private Object[][] getObjectArray(List<ComicEntity> list) {
        Object[][] data = new Object[list.size()][columns.length + 1];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i).getThumbnail();
            data[i][1] = list.get(i).getSeriesNo();
            data[i][2] = list.get(i).getSeriesName();
            data[i][3] = list.get(i).getIssueTitle();
            data[i][4] = list.get(i).getIssueNum();
            data[i][5] = list.get(i).getWriter();
            data[i][6] = list.get(i).getArtist();
            data[i][7] = list.get(i).getPubName();
            data[i][8] = list.get(i).getPubDate().format(DateTimeFormatter.ofPattern("MMMM d, YYYY"));
            data[i][9] = list.get(i).getValue();
            data[i][10] = list.get(i).getComicID();
        }
        return data;
    }

    private class NonEditableModel extends DefaultTableModel {
        public NonEditableModel(Object[][] data, String[] columns) {
            super(data, columns);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}


