package com.drkiet.biblereader.translation;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.drkiet.biblereader.reference.ReaderListener.Command;
import com.drkiet.biblereader.util.FontSizingToolbarPanel;
import com.drkiet.biblereader.util.ScreenPositions;

public class TranslationFrame extends JFrame {

	private static final long serialVersionUID = 4689102811730742079L;
	private TranslationPanel translationPanel;
	private FontSizingToolbarPanel fontSizingToolbarPanel;

	public TranslationFrame() {
		setLayout(new BorderLayout());
		setTitle("Translation");
		setSize(400, 350);
		translationPanel = new TranslationPanel();
		fontSizingToolbarPanel = new FontSizingToolbarPanel();

		fontSizingToolbarPanel.setReaderListener((Command cmd) -> {
			switch (cmd) {

			case LARGER_TEXT_FONT:
				makeLargerFont();
				break;

			case SMALLER_TEXT_FONT:
				makeSmallerFont();
				break;

			default:
				break;
			}
		});

		add(fontSizingToolbarPanel, BorderLayout.NORTH);
		add(translationPanel, BorderLayout.CENTER);
		setLocation(ScreenPositions.getBottomEast(400, 350));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void makeSmallerFont() {
		translationPanel.setSmallerText();
	}

	private void makeLargerFont() {
		translationPanel.setLargerText();
	}

	public void setText(String text) {
		translationPanel.setText(text);
	}
}
