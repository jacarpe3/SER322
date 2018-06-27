package ui;

import database.ComicEntity;
import database.DBManager;
import database.SQL;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * Creates the main panel layout and components with associated listeners
 * @author Josh Carpenter, Yutian Zhang
 * @version 1.0
 */
public class MainPanel extends JPanel {

    private JTextField upcTF = new JTextField();
    private JTextField writerTF = new JTextField();
    private JTextField issueTitleTF = new JTextField();
    private JTextField artistTF = new JTextField();
    private JTextField seriesNameTF = new JTextField();
    private JTextField publisherNameTF = new JTextField();
    private JLabel message = new JLabel("");
    private JButton btnSearch = new JButton("Search");

    /**
     * Constructor for main panel
     */
	public MainPanel() {
        JSeparator separator = new JSeparator();
        JLabel head = new JLabel("Comic Book Value Look Up Tool");
        JLabel upc = new JLabel("Serial #");
        JLabel writersName = new JLabel("Writer Name");
        JLabel issueTitle = new JLabel("Issue Title");
        JLabel artistsName = new JLabel("Artist Name");
        JLabel seriesName = new JLabel("Series Name");
        JLabel publisherName = new JLabel("Publisher Name");
        JPanel status = new JPanel(new BorderLayout());
        JButton btnClear = new JButton("Clear All");
        JButton btnDelete = new JButton("Delete Selected");
        DataPanel resultsTable = new DataPanel();

        status.add(message, BorderLayout.SOUTH);
		head.setFont(new Font("Dialog",Font.BOLD,40));
		head.setForeground(Color.BLUE);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout
                .createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(head)
                        .addComponent(separator)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(upc)
                                        .addComponent(publisherName))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(upcTF)
                                        .addComponent(publisherNameTF))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(issueTitle)
                                        .addComponent(seriesName))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(issueTitleTF)
                                        .addComponent(seriesNameTF))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(writersName)
                                        .addComponent(artistsName))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(writerTF)
                                        .addComponent(artistTF)))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSearch)
                                .addComponent(btnClear)
                                .addComponent(btnDelete))
                        .addComponent(separator)
                        .addComponent(resultsTable)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(status))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(head)
                        .addComponent(separator)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(upc)
                                .addComponent(upcTF)
                                .addComponent(issueTitle)
                                .addComponent(issueTitleTF)
                                .addComponent(writersName)
                                .addComponent(writerTF))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(publisherName)
                                .addComponent(publisherNameTF)
                                .addComponent(seriesName)
                                .addComponent(seriesNameTF)
                                .addComponent(artistsName)
                                .addComponent(artistTF))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnSearch)
                                .addComponent(btnClear)
                                .addComponent(btnDelete))
                        .addComponent(resultsTable)
                        .addComponent(status)
        );

        // Listeners
        btnSearch.addActionListener(e -> {
            if (upcTF.getText().isEmpty() && seriesNameTF.getText().isEmpty() && issueTitleTF.getText().isEmpty() &&
                    writerTF.getText().isEmpty() && artistTF.getText().isEmpty() && publisherNameTF.getText().isEmpty()) {
                setMessage("Search parameters missing!", Color.RED);
            } else {
                resultsTable.refresh(resultsTable.getResultsData());
            }
        });

        btnClear.addActionListener(e -> {
            upcTF.setText("");
            seriesNameTF.setText("");
            issueTitleTF.setText("");
            writerTF.setText("");
            artistTF.setText("");
            publisherNameTF.setText("");
            setMessage("", Color.BLACK);
            resultsTable.clear();
        });

        btnDelete.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null, "Delete selected comic(s)... Are you sure?",
                    "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                int num = resultsTable.deleteSelected();
                resultsTable.refresh(resultsTable.getAllData());
            } else {
                JOptionPane.getRootFrame().dispose();
            }
        });

	}

    /**
     * Grabs the given search text supplied by user and processes into list of results from database
     * @return List containing ComicEntities that match the given search criteria
     */
    public List<ComicEntity> getResultsList() {
        Map<String, String> params = new HashMap<>();
        if (!upcTF.getText().isEmpty()) {
            params.put(SQL.Columns.SERIES_NO, upcTF.getText());
        }
        if (!writerTF.getText().isEmpty()) {
            params.put(SQL.Columns.WRITERS, writerTF.getText());
        }
        if (!artistTF.getText().isEmpty()) {
            params.put(SQL.Columns.ARTISTS, artistTF.getText());
        }
        if (!seriesNameTF.getText().isEmpty()) {
            params.put(SQL.Columns.SERIES_NAME, seriesNameTF.getText());
        }
        if (!publisherNameTF.getText().isEmpty()) {
            params.put(SQL.Columns.PUB_NAME, publisherNameTF.getText());
        }
        if (!issueTitleTF.getText().isEmpty()) {
            params.put(SQL.Columns.ISSUE_TITLE, issueTitleTF.getText());
        }
        return DBManager.getInstance().query(params);
    }

    /**
     * Allows other classes (DataPanel) to change the status message at the bottom
     * @param msg String message you wish to display at bottom of the page
     */
    public void setMessage(String msg, Color color) {
	    message.setText(msg);
	    message.setForeground(color);
    }

    public JButton getSearchButton() {
        return btnSearch;
    }

}
