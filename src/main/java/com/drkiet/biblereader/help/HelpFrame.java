package com.drkiet.biblereader.help;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.drkiet.biblereader.reference.ReaderListener.Command;
import com.drkiet.biblereader.util.FontSizingToolbarPanel;
import com.drkiet.biblereader.util.ScreenPositions;

public class HelpFrame extends JFrame {
	private static final long serialVersionUID = -2838312659227965342L;

	private HelpPanel helpPanel;
	private FontSizingToolbarPanel fontSizingToolbarPanel;

	public HelpFrame() {
		setLayout(new BorderLayout());
		setTitle("FAQs");
		setSize(700, 600);
		helpPanel = new HelpPanel();
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
		add(helpPanel, BorderLayout.CENTER);
		setLocation(ScreenPositions.getTopWest(700, 600));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void makeSmallerFont() {
		helpPanel.setSmallerText();
	}

	private void makeLargerFont() {
		helpPanel.setLargerText();
	}
}
