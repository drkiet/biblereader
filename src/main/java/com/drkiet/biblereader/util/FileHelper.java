package com.drkiet.biblereader.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import com.drkiettran.text.TextApp;

public class FileHelper {
	public static String loadTextFileIntoString(String fileName) {
		try (InputStream is = TextApp.class.getResourceAsStream(fileName)) {
			StringBuilder sb = new StringBuilder();

			for (;;) {
				int c = is.read();
				if (c < 0) {
					break;
				}
				sb.append((char) c);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String loadFaqsFile() {

		try (InputStream is = TextApp.class.getResourceAsStream("/Faqs.md")) {
			Reader in = new InputStreamReader(is);
			Writer out = new StringWriter();
			Markdown md = new Markdown();
			try {
				md.transform(in, out);
			} catch (ParseException e) {
				e.printStackTrace();
				return "*** ERROR ***";
			}
			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
