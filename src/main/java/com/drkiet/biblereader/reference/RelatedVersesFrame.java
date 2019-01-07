package com.drkiet.biblereader.reference;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.drkiet.biblereader.reference.ReaderListener.Command;
import com.drkiet.biblereader.util.ScreenPositions;

public class RelatedVersesFrame extends JFrame {

	private static final long serialVersionUID = 4689102811730742079L;
	private RelatedVersesPanel relatedVersesPanel;
	private RelatedVersesToolbarPanel relatedVersesToolbarPanel;

	public RelatedVersesFrame() {
		setLayout(new BorderLayout());
		setTitle("Related Verses");
		setSize(400, 350);
		relatedVersesPanel = new RelatedVersesPanel();
		relatedVersesToolbarPanel = new RelatedVersesToolbarPanel();

		relatedVersesToolbarPanel.setReaderListener((Command cmd) -> {
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

		add(relatedVersesToolbarPanel, BorderLayout.NORTH);
		add(relatedVersesPanel, BorderLayout.CENTER);
		setLocation(ScreenPositions.getTopEast(400, 350));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void makeSmallerFont() {
		relatedVersesPanel.setSmallerText();
	}

	private void makeLargerFont() {
		relatedVersesPanel.setLargerText();
	}

	public void setText(String relatedVerses) {
		relatedVersesPanel.setText(relatedVerses);
	}
}
