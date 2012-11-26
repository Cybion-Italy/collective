package com.collective.downloader.rss.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class XmlEncodingContentCleaner {

    private static Logger logger = Logger.getLogger(XmlEncodingContentCleaner.class);
	
	public static final String ENCODING = "UTF-8";

	public String stripNonValidXMLCharacters(String in) {
		StringBuffer out = new StringBuffer(); // Used to hold the output.
		char current; // Used to reference the current character.

		if (in == null || ("".equals(in)))
			return ""; // vacancy test.
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
									// here; it should not happen.
			if ((current == 0x9) || (current == 0xA) || (current == 0xD)
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}

	public static InputStream stringToInputStream(String str) {
		byte[] bytes;
		try {
			bytes = str.getBytes(ENCODING);
		} catch (UnsupportedEncodingException e) {
            logger.error("Unsupported encoding", e);
            throw new RuntimeException("Unsupported encoding", e);
		}
		return new ByteArrayInputStream(bytes);
	}

	public InputStream cleanInputStream(InputStream feedInputStream) {

		StringBuilder stringBuilder = new StringBuilder();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				feedInputStream));
		String line;
		try {
			while ((line = in.readLine()) != null) {
				String cleanedLine = this.stripNonValidXMLCharacters(line);
				stringBuilder.append(cleanedLine + "\n");
			}
		} catch (IOException e) {
            logger.error("Error while reading from stream: '" + feedInputStream + "'", e);            
            throw new RuntimeException("Error while reading from stream: '" + feedInputStream + "'", e);
		}

		String cleanedContent = stringBuilder.toString();
		InputStream contentStream = stringToInputStream(cleanedContent);
		return contentStream;
	}

}
