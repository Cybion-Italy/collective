package com.collective.downloader.rss.utils;

import java.util.regex.*;

/*it strips any html tags from content string */
public class SimpleRegexHtmlCleaner implements HtmlCleaner {

	public String stripHtml(String htmlString) {

		// This pattern Matches everything found inside html tags;
		// (.|\n) - > Look for any character or a new line
		// *? -> 0 or more occurences, and make a non-greedy search meaning
		// That the match will stop at the first available '>' it sees, and not
		// at the last one
		// (if it stopped at the last one we could have overlooked
		// nested HTML tags inside a bigger HTML tag..)
		String patternStr = "<(.|\n)*?>";

		Pattern pattern = Pattern.compile(patternStr);

		Matcher matcher = pattern.matcher(htmlString);
		String cleanedString = matcher.replaceAll("");

		return cleanedString;
	}

	
}
