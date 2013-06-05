package com.collective.downloader.rss;

import com.collective.downloader.rss.utils.SimpleRegexHtmlCleaner;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Reference test for {@link com.collective.downloader.rss.SimpleRegexHtmlCleanerTest} class.
 */
public class SimpleRegexHtmlCleanerTest {

    private static final Logger LOGGER = Logger.getLogger(SimpleRegexHtmlCleanerTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void shouldCleanString() {
		String htmlString = "<a>to clean</a> <b>html string</b><a href=\"\" property=\"bbb\"></a><br/>";
        LOGGER.info("trying to strip: '" + htmlString + "'");
		SimpleRegexHtmlCleaner cleaner = new SimpleRegexHtmlCleaner();
		String cleanedString = cleaner.stripHtml(htmlString);
        LOGGER.info("stripped string: '" + cleanedString + "'");
		Assert.assertTrue(cleanedString.equals("to clean html string"));
	}
}
