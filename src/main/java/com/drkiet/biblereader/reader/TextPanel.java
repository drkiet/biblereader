package com.drkiet.biblereader.reader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.biblereader.commentary.CommentaryFrame;
import com.drkiet.biblereader.reference.ReaderListener;
import com.drkiet.biblereader.reference.RelatedVersesFrame;
import com.drkiet.biblereader.translation.TranslationFrame;
import com.drkiet.biblereader.util.FileHelper;
import com.drkiet.biblereader.reference.ReaderListener.Command;
import com.drkiettran.text.ReadingTextManager;
import com.drkiettran.text.model.Document;
import com.drkiettran.text.model.Page;
import com.drkiettran.text.model.SearchResult;
import com.drkiettran.text.model.Word;

/**
 * TextPanel consists of three main components:
 * <ul>
 * <li>Reading word Label (Label)
 * <li>Reading text area (TextArea)
 * <li>Information area (Label)
 * </ul>
 * 
 * @author ktran
 *
 */
public class TextPanel extends JPanel {
	private static final Logger LOGGER = LoggerFactory.getLogger(TextPanel.class);

	public static final String VERSE_INFO = " *** verse: ";
	public static final int DEFAULT_DISPLAYING_FONT_SIZE = 60;
	public static final int DEFAULT_TEXT_AREA_FONT_SIZE = 18;
	public static final int SMALLEST_DISPLAYING_FONT_SIZE = 20;
	public static final int SMALLEST_TEXT_AREA_FONT_SIZE = 10;
	public static final int LARGEST_DISPLAYING_FONT_SIZE = 100;
	public static final int LARGEST_TEXT_AREA_FONT_SIZE = 32;
	public static final long serialVersionUID = -825536523977292110L;

	private JTextArea textArea;
	private JLabel displayingWordLabel;
	private String readingText = null;
	private JLabel infoLabel;
	private JLabel titleLabel;

	private RelatedVersesFrame relatedVersesFrame = new RelatedVersesFrame();
	private CommentaryFrame commentaryFrame = new CommentaryFrame();
	private TranslationFrame translationFrame = new TranslationFrame();

	private List<Object> highlightedWords = new ArrayList<Object>();
	private Object highlightSelectedWord = null;
	private Object highlightedWord = null;

	private ReaderListener readerListener;

	private String displayingFontName = "Candara";
	private String infoFontName = "Candara";
	private int displayingWordFontSize = DEFAULT_DISPLAYING_FONT_SIZE;
	private int infoFontSize = 12;
	private String textAreaFontName = "Candara";
	private int textAreaFontSize = DEFAULT_TEXT_AREA_FONT_SIZE;
	private int defaultBlinkRate = 0;

	private Document document = null;
	private ReadingTextManager readingTextManager = null;
	private boolean doneReading = true;
	private Word selectedWord = null;
	private Word wordAtMousePos = null;
	private SearchResult searchResult = null;
	private String searchText;
	private List<String> currentTextAreaByLines = null;
	private int currentLineNumber;
	private Integer currentVerseNumber;
	private String currentLine;

	public boolean isDoneReading() {
		return doneReading;
	}

	private void resetState() {
		document = null;
		readingText = null;
		doneReading = true;
		selectedWord = null;
		wordAtMousePos = null;
		searchResult = null;
		searchText = null;
		currentTextAreaByLines = null;
		currentLine = null;
		currentLineNumber = 0;
		currentVerseNumber = 0;
		infoLabel.setText(null);
		displayingWordLabel.setText(null);
	}

	public TextPanel() {
		arrangeFixedComponents();
		setBorder();
		arrangeLayout();
	}

	private void arrangeFixedComponents() {
		displayingWordLabel = new JLabel();
		displayingWordLabel.setFont(new Font(displayingFontName, Font.PLAIN, displayingWordFontSize));
		displayingWordLabel.setHorizontalAlignment(JLabel.CENTER);
		textArea = new JTextArea();
		infoLabel = new JLabel("");
		infoLabel.setFont(new Font(infoFontName, Font.PLAIN, infoFontSize));
		titleLabel = new JLabel("Title:");
	}

	private void arrangeLayout() {
		setLayout(new BorderLayout());
		add(displayingWordLabel, BorderLayout.NORTH);
		addWelcomePane();
		add(titleLabel, BorderLayout.SOUTH);
		add(infoLabel, BorderLayout.SOUTH);
	}

	private void addWelcomePane() {
		removeComponentOnCenterLayout();
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setForeground(Color.GREEN);
		textPane.setBackground(new Color(245, 245, 245));

		textPane.setText(FileHelper.loadTextFileIntoString("/About.html"));

		add(textPane, BorderLayout.CENTER);
	}

	public void removeComponentOnCenterLayout() {
		BorderLayout layout = (BorderLayout) getLayout();
		Component comp = layout.getLayoutComponent(BorderLayout.CENTER);
		if (comp != null) {
			remove(comp);
		}
	}

	private void addTextPane() {
		removeComponentOnCenterLayout();
		makeTextArea();
		add(new JScrollPane(textArea), BorderLayout.CENTER);
	}

	private void setBorder() {
		Border innerBorder = BorderFactory.createTitledBorder("Scripture");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
	}

	private void makeTextArea() {
		defaultBlinkRate = textArea.getCaret().getBlinkRate();
		textArea.setCaretPosition(0);
		textArea.setCaretColor(Color.white);
		textArea.setFont(new Font(textAreaFontName, Font.PLAIN, textAreaFontSize));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);

		textArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				readerListener.invoke(Command.RESTART);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				readerListener.invoke(Command.RESTART);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				readerListener.invoke(Command.RESTART);
			}

		});

		textArea.addMouseMotionListener(getMouseMotionListener());
		textArea.addMouseListener(getMouseListner());

	}

	private MouseListener getMouseListner() {

		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (readingTextManager != null) {
					int caretPos = textArea.getCaretPosition();
					selectedWord = readingTextManager.getWordAt(textArea.getCaretPosition());
					try {
						highlightSelectedWord = highlight(selectedWord.getTransformedWord(),
								selectedWord.getIndexOfText(), Color.GRAY, highlightSelectedWord);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					search(selectedWord.getTransformedWord());
					textArea.setCaretPosition(caretPos);
				}
			}

			@Override
			public void mousePressed(MouseEvent event) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				wordAtMousePos = null;
			}
		};
	}

	private MouseMotionListener getMouseMotionListener() {
		return new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent event) {
				processToolTip(event);
			}

			@Override
			public void mouseDragged(MouseEvent event) {
			}
		};

	}

	public boolean mouseOverWord(int caretPos) {
		return caretPos >= wordAtMousePos.getIndexOfText()
				&& caretPos <= wordAtMousePos.getIndexOfText() + wordAtMousePos.getTransformedWord().length();
	}

	public void processToolTip(MouseEvent event) {
		if (document == null) {
			return;
		}

		int viewToModel = textArea.viewToModel(event.getPoint());

		if (viewToModel == -1) {
			textArea.setToolTipText(null);
			return;
		}

		try {
			currentLineNumber = textArea.getLineOfOffset(viewToModel);
		} catch (BadLocationException e) {
			LOGGER.error("Bad location: {}", e);
		}

		currentLine = currentTextAreaByLines.get(currentLineNumber).trim();

		if (currentLine.isEmpty() || readingTextManager == null) {
			textArea.setToolTipText(null);
			return;
		}

		int firstBlankIdx = currentLine.indexOf(' ');

		currentVerseNumber = Integer.valueOf(currentLine.substring(0, firstBlankIdx));
		LOGGER.info("Current verse #: {}", currentVerseNumber);

		updateInfoLabel();

		displayTranslation();
		displayRelatedVerses();
		displayCommentary();
		repaint();

	}

	private void displayTranslation() {
		String translationTitle = String.format("Translations: %s %d:%d", document.getBookName(),
				document.getCurrentPageNumber(), currentVerseNumber);
		translationFrame.setTitle(translationTitle);
		translationFrame.setText(document.getTranslationTexts(currentVerseNumber));
	}

	private void displayCommentary() {
		String relatedVersesTitle = String.format("Commentary: %s %d:%d", document.getBookName(),
				document.getCurrentPageNumber(), currentVerseNumber);
		commentaryFrame.setTitle(relatedVersesTitle);
		commentaryFrame.setComment(document.getCommentInfo(currentVerseNumber));
	}

	public void displayRelatedVerses() {
		String relatedVersesTitle = String.format("References: %s %d:%d", document.getBookName(),
				document.getCurrentPageNumber(), currentVerseNumber);
		relatedVersesFrame.setTitle(relatedVersesTitle);
		relatedVersesFrame.setText(document.getRelatedVerses(currentVerseNumber));
	}

	public void updateInfoLabel() {
		String labelText = infoLabel.getText();
		int idx = labelText.indexOf(VERSE_INFO);
		String cursorInfo;

		if (idx >= 0) {
			labelText = labelText.substring(0, idx);
		}

		if (document != null) {
			cursorInfo = String.format("%s %d:%d", VERSE_INFO, document.getCurrentPageNumber(), currentVerseNumber);
		} else {
			cursorInfo = VERSE_INFO + currentVerseNumber;
		}

		infoLabel.setText(labelText + cursorInfo);
		LOGGER.info("line {}: {}", currentLineNumber, currentTextAreaByLines.get(currentLineNumber));
	}

	public void resetReading() {
		LOGGER.info("Reset reading ...");
		addWelcomePane();
		resetState();
		repaint();
	}

	public void nextWord() throws BadLocationException {
		String wordToRead = getNextWord();
		LOGGER.debug("wordtoread: {}", wordToRead);
		if (wordToRead != null) {
			if (wordToRead.isEmpty()) {
				return;
			}

			highlightedWord = highlight(wordToRead, readingTextManager.getCurrentCaret(), Color.PINK, highlightedWord);
			textArea.requestFocus();
			displayingWordLabel.setText(wordToRead);
			displayReadingInformation();
		} else {
			displayReadingInformation();
			doneReading = true;
		}
		repaint();
	}

	public void displayText() {
		currentTextAreaByLines = Arrays.asList(readingTextManager.getReadingText().split("\n"));
		textArea.setText(readingTextManager.getReadingText());
	}

	private Object highlight(String wordToRead, int caret, Color color, Object highlightedWord)
			throws BadLocationException {
		textArea.setCaretPosition(caret);
		Highlighter hl = textArea.getHighlighter();
		HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(color);
		int p0 = textArea.getCaretPosition();
		int p1 = p0 + wordToRead.length();
		if (highlightedWord != null) {
			hl.removeHighlight(highlightedWord);
		}
		return hl.addHighlight(p0, p1, painter);
	}

	private void unHighlight(Object highlightedWord) {
		textArea.getHighlighter().removeHighlight(highlightedWord);
	}

	private void displayReadingInformation() {
		int wordsFromBeginning = readingTextManager.getWordsFromBeginning();
		int totalWords = readingTextManager.getTotalWords();
		int readingPercentage = 0;

		if (totalWords != 0) {
			readingPercentage = (100 * wordsFromBeginning) / totalWords;
		}

		String docInfo = "";
		if (document != null) {
			docInfo = String.format("chapter %d of %d", document.getCurrentPageNumber(), document.getPageCount());
		}

		infoLabel.setText(
				String.format("%s: %d of %d words (%d%%)", docInfo, wordsFromBeginning, totalWords, readingPercentage));
		infoLabel.setForeground(Color.BLUE);
	}

	private void displaySearchResult() {
		infoLabel.setText(String.format("found %d '%s's", searchResult.getNumberMatchedWords(), searchText));
		infoLabel.setForeground(Color.BLUE);
	}

	private String getNextWord() {
		return readingTextManager.getNextWord();
	}

	public void setReaderListener(ReaderListener readerListener) {
		this.readerListener = readerListener;
	}

	public void stopReading() {
		textArea.setCaret(new DefaultCaret());
		textArea.getCaret().setBlinkRate(defaultBlinkRate);
		textArea.setCaretPosition(readingTextManager.getCurrentCaret());
		textArea.requestFocus();
		this.doneReading = false;
	}

	public void startReading() {
		if (document == null) {
			return;
		}

		textArea.setCaret(new FancyCaret());

		if (document != null && readingTextManager == null) {
			displayPageText(document.getCurrentPage());
		}

		if (readingTextManager != null) {
			textArea.setCaretPosition(readingTextManager.getCurrentCaret());
		} else {
			textArea.setCaretPosition(0);
		}

		doneReading = false;
		textArea.requestFocus();
	}

	public void setCurrentCaretAt() {
		if (readingTextManager != null) {
			readingTextManager.setCurrentCaret(textArea.getCaretPosition());
		}
	}

	public void search(String searchText) {
		if (readingTextManager == null) {
			return;
		}

		for (Object highlightedWord : highlightedWords) {
			unHighlight(highlightedWord);
		}

		this.searchText = searchText;
		this.searchResult = readingTextManager.search(searchText);
		Hashtable<Integer, String> matchedWords = searchResult.getMatchedWords();
		highlightedWords = new ArrayList<Object>();

		for (Integer idx : matchedWords.keySet()) {
			try {
				LOGGER.debug("highlighted at /{}/", matchedWords.get(idx));
				highlightedWords.add(highlight(matchedWords.get(idx), idx, Color.GREEN, null));
			} catch (BadLocationException e) {
				LOGGER.error("Bad location exception: {}", e);
			}
		}
		displaySearchResult();
	}

	public void setInfo(String info) {

		if (readingTextManager != null) {
			infoLabel.setText(info);
			infoLabel.setForeground(Color.BLUE);
			textArea.setCaretPosition(readingTextManager.getCurrentCaret());
			textArea.requestFocus();
		} else {
			infoLabel.setText("Start reading first!");
			infoLabel.setForeground(Color.RED);
		}
		repaint();
	}

	public void startReadingAt() {
		if (document != null && readingTextManager != null) {
			readingTextManager.setCurrentCaret(selectedWord.getIndexOfText());
			startReading();
		}
	}

	public void loadTextFromFile(Document document) {
		this.document = document;
		addTextPane();
		displayPageText(document.getCurrentPage());
	}

	public void previousPage() {
		if (document != null) {
			Page page = document.previousPage();
			if (page != null) {
				displayPageText(page);
			}
		}
	}

	public void nextPage() {
		if (document != null) {
			Page page = document.nextPage();
			if (page != null) {
				displayPageText(page);
			}
		}
	}

	public void displayPageText(Page page) {
		readingTextManager = page.getRtm();

		readingText = readingTextManager.getReadingText();
		if (emptyReadingText()) {
			textArea.setText("*** PAGE IS EMPTY! ***");
		} else {
			displayText();
		}
		textArea.setCaretPosition(0);
		displayReadingInformation();
		repaint();
	}

	private boolean emptyReadingText() {
		for (int idx = 0; idx < readingText.length(); idx++) {
			if (Character.isAlphabetic(readingText.charAt(idx)) || Character.isDigit(readingText.charAt(idx))) {
				return false;
			}
		}
		return true;
	}

	public void goTo(int gotoPageNo) {
		if (document != null) {
			document.setPageNo(gotoPageNo);
			displayPageText(document.getCurrentPage());
		}
	}

	public void setLargerTextFont() {
		if (textAreaFontSize < LARGEST_TEXT_AREA_FONT_SIZE) {
			this.textAreaFontSize++;
		}
		textArea.setFont(new Font(textAreaFontName, Font.PLAIN, textAreaFontSize));
		repaint();
	}

	public void setSmallerTextFont() {
		if (textAreaFontSize > SMALLEST_TEXT_AREA_FONT_SIZE) {
			this.textAreaFontSize--;
		}
		textArea.setFont(new Font(textAreaFontName, Font.PLAIN, textAreaFontSize));
		repaint();
	}

	public void setLargerWordFont() {
		if (displayingWordFontSize < LARGEST_DISPLAYING_FONT_SIZE) {
			this.displayingWordFontSize++;
		}
		displayingWordLabel.setFont(new Font(displayingFontName, Font.PLAIN, displayingWordFontSize));
		repaint();
	}

	public void setSmallerWordFont() {
		if (displayingWordFontSize > SMALLEST_DISPLAYING_FONT_SIZE) {
			this.displayingWordFontSize--;
		}
		displayingWordLabel.setFont(new Font(displayingFontName, Font.PLAIN, displayingWordFontSize));
		repaint();
	}

}
