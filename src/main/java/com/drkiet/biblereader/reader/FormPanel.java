package com.drkiet.biblereader.reader;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.biblereader.reference.ReaderListener;
import com.drkiet.biblereader.reference.ReaderListener.Command;
import com.drkiettran.scriptureinaction.model.BibleBook;
import com.drkiettran.scriptureinaction.query.AboutBible;
import com.drkiettran.scriptureinaction.query.AboutBook;
import com.drkiettran.text.TextApp;
import com.drkiettran.text.model.Document;

/**
 * 
 * @author ktran
 *
 */
public class FormPanel extends JPanel {
	private static final Logger LOGGER = LoggerFactory.getLogger(FormPanel.class);

	private static final long serialVersionUID = 3506596135223108382L;
	private JLabel bookNameLabel;
	private JComboBox<String> bookNameComboBox;
	private JButton loadButton;
	private JLabel speedLabel;
	private JTextField speedField;
	private JLabel searchTextLabel;
	private JTextField searchTextField;
	private JButton searchButton;
	private JButton setButton;
	private Integer speedWpm = 200;
	private String bookName;
	private String text;
	private ReaderListener readerListener;
	private Document document = null;
	private String loadingError = "";
	private JButton goToPageNoButton;
	private JTextField pageNoTextField;
	private JLabel pageNoTextLabel;
	private JButton nextFindButton;
	private JLabel translationLabel;
	private JComboBox<String> translationComboBox;

	private AboutBible aboutBible = new AboutBible();

	private AboutBook aboutBook;

	private String translation;

	public String getLoadingError() {
		return loadingError;
	}

	public Document getDocument() {
		return document;
	}

	public String getText() {
		return text;
	}

	public Integer getSpeedWpm() {
		return speedWpm;
	}

	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 250;
		setPreferredSize(dim);

		bookNameLabel = new JLabel("Select a Book: ");
		bookNameComboBox = new JComboBox<String>((String[]) getListOfBookNames().toArray());
		translationLabel = new JLabel("Translation: ");
		translationComboBox = new JComboBox<String>((String[]) getListOfTranslations().toArray());

		speedLabel = new JLabel("Speed (wpm): ");
		speedField = new JTextField(10);
		searchTextLabel = new JLabel("Text");
		searchTextField = new JTextField(10);
		pageNoTextLabel = new JLabel("Chapter No.");
		pageNoTextField = new JTextField(10);
		speedField.setText("" + speedWpm);

		setButton = new JButton("Set");
		loadButton = new JButton("Load");
		searchButton = new JButton("Search");
		nextFindButton = new JButton("Next");
		goToPageNoButton = new JButton("Go to");

		setButton.addActionListener((ActionEvent actionEvent) -> {
			speedWpm = Integer.valueOf(speedField.getText());
		});

		loadButton.addActionListener((ActionEvent actionEvent) -> {
			bookName = (String) bookNameComboBox.getSelectedItem();
			translation = (String)translationComboBox.getSelectedItem();
			
			TextApp textApp = new TextApp(translation);

			document = textApp.getPages(bookName);
			if (document == null) {
				loadingError = "Unable to load " + bookName;
			} else {
				LOGGER.info("{} has {} pages", bookName, document.getPageCount());
				loadingError = "";
			}
			readerListener.invoke(Command.LOAD);
		});

		searchButton.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.SEARCH);
		});

		nextFindButton.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.NEXTFIND);
		});

		goToPageNoButton.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.GOTO);
		});

		bookNameComboBox.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.SELECT_BOOK);
		});

		translationComboBox.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.SELECT_TRANSLATION);
		});

		Border innerBorder = BorderFactory.createTitledBorder("Configuration");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));

		layoutComponents();
	}

	private List<String> getListOfTranslations() {
		return aboutBible.getTranslations();
	}

	private List<String> getListOfBookNames() {
		return aboutBible.getBookNames();
	}

	public String getBookName() {
		return bookName;
	}

	private void layoutComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		//// FIRST ROW /////////////
		gc.gridy = 0;

		// Always do the following to avoid future confusion :)
		// Speed
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(speedLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(speedField, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = .2;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(setButton, gc);

		// Always do the following to avoid future confusion :)
		// Book Name
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(bookNameLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(bookNameComboBox, gc);

		// Always do the following to avoid future confusion :)
		// Translation
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(translationLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(translationComboBox, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = .2;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(loadButton, gc);

		// Always do the following to avoid future confusion :)
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(searchTextLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(searchTextField, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = .2; // 5;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(searchButton, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = .2;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(nextFindButton, gc);

		// Always do the following to avoid future confusion :)
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(pageNoTextLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(pageNoTextField, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 5;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(goToPageNoButton, gc);

		disableSearch();
		disableGoto();
	}

	public void disableGoto() {
		pageNoTextField.setEnabled(false);
		goToPageNoButton.setEnabled(false);
	}

	public void enableGoTo() {
		pageNoTextField.setEnabled(true);
		goToPageNoButton.setEnabled(true);
	}

	public void setReaderListener(ReaderListener readerListener) {
		this.readerListener = readerListener;
	}

	public void setFileName(String selectedFile) {
		bookNameComboBox.setSelectedItem(selectedFile);
	}

	public String getSearchText() {
		return searchTextField.getText();
	}

	public void enableSearch() {
		searchButton.setEnabled(true);
		nextFindButton.setEnabled(true);
		searchTextField.setEnabled(true);
	}

	public void disableSearch() {
		searchButton.setEnabled(false);
		nextFindButton.setEnabled(false);
		searchTextField.setEnabled(false);
	}

	public int getGotoPageNo() {
		if (!pageNoTextField.getText().trim().isEmpty()) {
			return Integer.valueOf(pageNoTextField.getText());
		}
		return -1;
	}

	public String getSelectedBookName() {
		return (String) bookNameComboBox.getSelectedItem();
	}

	public String getselectedTranslation() {
		return (String) translationComboBox.getSelectedItem();
	}

	public AboutBook getAboutBook() {
		return aboutBook;
	}

	public BibleBook getSelectedBook() {
		aboutBook = aboutBible.getAboutBook(getselectedTranslation(), getSelectedBookName());
		return aboutBook.getBook();
	}
}
