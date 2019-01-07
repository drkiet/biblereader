package com.drkiet.biblereader.reference;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

public class RelatedVersesPanel extends JPanel {
	private static final long serialVersionUID = -8244136736985618463L;
	public static final int SMALLEST_TEXT_AREA_FONT_SIZE = 3;
	public static final int LARGEST_TEXT_AREA_FONT_SIZE = 7;
	private JTextPane textPane;
	private int textPaneFontSize = 4;
	private String textPaneFont = "Candara";
	private String relatedVerses = null;

	public RelatedVersesPanel() {
		textPane = new JTextPane();
		textPane.setCaretPosition(0);
		textPane.setCaretColor(Color.WHITE);
		textPane.setContentType("text/html");

		setLayout(new BorderLayout());
		add(new JScrollPane(textPane), BorderLayout.CENTER);
		setBorder();
	}

	public void setText(String relatedVerses) {
		this.relatedVerses = relatedVerses;
		textPane.setText(String.format(relatedVerses, textPaneFontSize, textPaneFont));
		textPane.setCaretPosition(0);
	}

	private void setBorder() {
		Border innerBorder = BorderFactory.createTitledBorder("Related Verses");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
	}

	public void setSmallerText() {
		if (textPaneFontSize > SMALLEST_TEXT_AREA_FONT_SIZE) {
			this.textPaneFontSize--;
		}
		textPane.setText(String.format(relatedVerses, textPaneFontSize, textPaneFont));
		repaint();
	}

	public void setLargerText() {
		if (textPaneFontSize < LARGEST_TEXT_AREA_FONT_SIZE) {
			this.textPaneFontSize++;
		}
		textPane.setText(String.format(relatedVerses, textPaneFontSize, textPaneFont));
		repaint();
	}
}
