package ui;

import database.ComicEntity;
import database.Constants;
import database.DBManager;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputPanel extends JPanel {

	JLabel isbn = new JLabel("ISBN");
    JLabel contributorFirstName = new JLabel("Contributor First Name");
    JLabel issueTitle = new JLabel("Issue Title");
	JLabel contributorLastName = new JLabel("Contributor Last Name");
	JLabel seriesName = new JLabel("Series Name");
	JLabel publisherName = new JLabel("Publisher Name");
	JTextField isbnTF = new JTextField();
	JTextField contributorFirstNameTF = new JTextField();
	JTextField issueTitleTF = new JTextField();
	JTextField contributorLastNameTF = new JTextField();
	JTextField seriesNameTF = new JTextField();
	JTextField publisherNameTF = new JTextField();

	public InputPanel() {
		this.setLayout(new GridLayout(3,4));
		this.add(isbn);
		this.add(isbnTF);
		this.add(contributorFirstName);
		this.add(contributorFirstNameTF);
		this.add(issueTitle);
		this.add(issueTitleTF);
		this.add(contributorLastName);
		this.add(contributorLastNameTF);
		this.add(seriesName);
		this.add(seriesNameTF);
		this.add(publisherName);
		this.add(publisherNameTF);
	}

	public List<ComicEntity> getResultsList() {
        Map<String, String> params = new HashMap<>();
        if (!isbnTF.getText().isEmpty()) {
            params.put(Constants.Search.seriesUPC, isbnTF.getText());
        }
        if (!contributorFirstNameTF.getText().isEmpty()) {
            params.put(Constants.Search.contribFName, contributorFirstNameTF.getText());
        }
        if (!contributorLastNameTF.getText().isEmpty()) {
            params.put(Constants.Search.contribLName, contributorLastNameTF.getText());
        }
        if (!seriesNameTF.getText().isEmpty()) {
            params.put(Constants.Search.seriesName, seriesNameTF.getText());
        }
        if (!publisherNameTF.getText().isEmpty()) {
            params.put(Constants.Search.pubName, publisherNameTF.getText());
        }
        if (!issueTitleTF.getText().isEmpty()) {
            params.put(Constants.Search.issueTitle, issueTitleTF.getText());
        }
	    return DBManager.getInstance().query(params);
    }

}
