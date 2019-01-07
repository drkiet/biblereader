package com.drkiet.biblereader;

import java.io.IOException;

import javax.swing.SwingUtilities;

import com.drkiet.biblereader.reader.MainFrame;

/**
 * Bible Reader - branched off from the Speed Reader project I developed
 *
 */
public class BibleReader {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame mainFrame = new MainFrame();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
