package ui;

import database.ComicEntity;
import database.DBManager;
import database.SQL;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class MainPanel extends JPanel {

    private JTextField upcTF = new JTextField();
    private JTextField contributorFirstNameTF = new JTextField();
    private JTextField issueTitleTF = new JTextField();
    private JTextField contributorLastNameTF = new JTextField();
    private JTextField seriesNameTF = new JTextField();
    private JTextField publisherNameTF = new JTextField();

	public MainPanel() {
        JSeparator separator = new JSeparator();

        JLabel head = new JLabel("Comic Book Value Look Up Tool");
        JLabel upc = new JLabel("Series UPC");
        JLabel contributorFirstName = new JLabel("Contributor First Name");
        JLabel issueTitle = new JLabel("Issue Title");
        JLabel contributorLastName = new JLabel("Contributor Last Name");
        JLabel seriesName = new JLabel("Series Name");
        JLabel publisherName = new JLabel("Publisher Name");
        JLabel message = new JLabel("Waiting for new Message");

        JPanel status = new JPanel(new BorderLayout());
        status.add(message, BorderLayout.SOUTH);

        JButton btnSearch = new JButton("Search");
        JButton btnClear = new JButton("Clear");

        DataPanel resultsTable = new DataPanel();

		head.setFont(new Font("Dialog",1,40));
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
                                        .addComponent(contributorFirstName)
                                        .addComponent(contributorLastName))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(contributorFirstNameTF)
                                        .addComponent(contributorLastNameTF)))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSearch)
                                .addComponent(btnClear))
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
                                .addComponent(contributorFirstName)
                                .addComponent(contributorFirstNameTF))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(publisherName)
                                .addComponent(publisherNameTF)
                                .addComponent(seriesName)
                                .addComponent(seriesNameTF)
                                .addComponent(contributorLastName)
                                .addComponent(contributorLastNameTF))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnSearch)
                                .addComponent(btnClear))
                        .addComponent(resultsTable)
                        .addComponent(status)
        );

	}

    public List<ComicEntity> getResultsList() {
        Map<String, String> params = new HashMap<>();
        if (!upcTF.getText().isEmpty()) {
            params.put(SQL.Columns.SERIES_UPC, upcTF.getText());
        }
        if (!contributorFirstNameTF.getText().isEmpty()) {
            params.put(SQL.Columns.CONTRIB_FNAME, contributorFirstNameTF.getText());
        }
        if (!contributorLastNameTF.getText().isEmpty()) {
            params.put(SQL.Columns.CONTRIB_LNAME, contributorLastNameTF.getText());
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

}
