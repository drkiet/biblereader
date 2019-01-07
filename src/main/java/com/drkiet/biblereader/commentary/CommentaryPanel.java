package com.drkiet.biblereader.commentary;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import com.drkiet.biblereader.reference.RelatedVersesPanel;

public class CommentaryPanel extends JPanel {

	private static final long serialVersionUID = 8683655130181562963L;
	private JTextPane commentPane;
	private String commentary = null;
	private Integer textPaneFontSize = 4;
	private String textPaneFont = "Candara";

	public CommentaryPanel() {
		commentPane = new JTextPane();
		commentPane.setCaretPosition(0);
		commentPane.setCaretColor(Color.WHITE);
		commentPane.setContentType("text/html");

		setLayout(new BorderLayout());
		add(new JScrollPane(commentPane), BorderLayout.CENTER);
		setBorder();
	}

	public void setComment(String commentary) {
		this.commentary = commentary;
		commentPane.setText(String.format(commentary, textPaneFontSize, textPaneFont));
		commentPane.setCaretPosition(0);
	}

	private void setBorder() {
		Border innerBorder = BorderFactory.createTitledBorder("Commentary");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
	}

	public void setSmallerText() {
		if (textPaneFontSize > RelatedVersesPanel.SMALLEST_TEXT_AREA_FONT_SIZE) {
			this.textPaneFontSize--;
		}
		commentPane.setText(String.format(commentary, textPaneFontSize, textPaneFont));
		repaint();
	}

	public void setLargerText() {
		if (textPaneFontSize < RelatedVersesPanel.LARGEST_TEXT_AREA_FONT_SIZE) {
			this.textPaneFontSize++;
		}
		commentPane.setText(String.format(commentary, textPaneFontSize, textPaneFont));
		repaint();
	}
}
