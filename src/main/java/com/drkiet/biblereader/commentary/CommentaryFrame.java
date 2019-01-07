package com.drkiet.biblereader.commentary;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.drkiet.biblereader.reference.ReaderListener;
import com.drkiet.biblereader.reference.ReaderListener.Command;
import com.drkiet.biblereader.util.ScreenPositions;

public class CommentaryFrame extends JFrame {

	private static final long serialVersionUID = 4689102811730742079L;
	private CommentaryPanel commentaryPanel;
	private CommentaryToolbarPanel commentaryToolbarPanel;

	public CommentaryFrame() {
		setLayout(new BorderLayout());
		setTitle("Commentary");
		setSize(400, 350);
		commentaryPanel = new CommentaryPanel();
		commentaryToolbarPanel = new CommentaryToolbarPanel();

		commentaryToolbarPanel.setReaderListener((Command cmd) -> {
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

		add(commentaryToolbarPanel, BorderLayout.NORTH);
		add(commentaryPanel, BorderLayout.CENTER);
		setLocation(ScreenPositions.getCenterEast(400, 350));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void makeSmallerFont() {
		commentaryPanel.setSmallerText();
	}

	private void makeLargerFont() {
		commentaryPanel.setLargerText();
	}

	public void setComment(String comment) {
		commentaryPanel.setComment(comment);
	}
}
