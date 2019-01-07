package com.drkiet.biblereader.translation;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import com.drkiet.biblereader.reference.RelatedVersesPanel;

public class TranslationPanel extends JPanel {

	private static final long serialVersionUID = -8205855362814620747L;

	private JTextPane textPane;
	private String text = null;
	private Integer textPaneFontSize = 4;
	private String textPaneFont = "Candara";

	public TranslationPanel() {
		textPane = new JTextPane();
		textPane.setCaretPosition(0);
		textPane.setCaretColor(Color.WHITE);
		textPane.setContentType("text/html");

		setLayout(new BorderLayout());
		add(new JScrollPane(textPane), BorderLayout.CENTER);
		setBorder();
	}

	public void setText(String text) {
		this.text = text;
		textPane.setText(String.format(text, textPaneFontSize, textPaneFont));
		textPane.setCaretPosition(0);
	}

	private void setBorder() {
		Border innerBorder = BorderFactory.createTitledBorder("Translations");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
	}

	public void setSmallerText() {
		if (textPaneFontSize > RelatedVersesPanel.SMALLEST_TEXT_AREA_FONT_SIZE) {
			this.textPaneFontSize--;
		}
		textPane.setText(String.format(text, textPaneFontSize, textPaneFont));
		repaint();
	}

	public void setLargerText() {
		if (textPaneFontSize < RelatedVersesPanel.LARGEST_TEXT_AREA_FONT_SIZE) {
			this.textPaneFontSize++;
		}
		textPane.setText(String.format(text, textPaneFontSize, textPaneFont));
		repaint();
	}

}
