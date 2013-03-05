package it.cybion.extractor;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.extractors.DefaultExtractor;
import org.apache.log4j.Logger;

import java.net.URL;

public class ContentExtractor {

    private static final Logger LOGGER = Logger.getLogger(ContentExtractor.class);

    public String getContentFromUrl(URL url) throws BoilerpipeProcessingException {
        // NOTE: Use ArticleExtractor unless DefaultExtractor gives better results for you
        String content = "";
        String urlString = url.toString();
        LOGGER.debug("getting content from url: " + urlString);
        int l = urlString.length();
        int point = urlString.lastIndexOf(".");
        String urlExtension = urlString.substring(point + 1, l);
//        logger.debug("urlExtension: '" + urlExtension + "'");
        if (urlExtension.compareToIgnoreCase("pdf") != 0 &&
                urlExtension.compareToIgnoreCase("ps") != 0 &&
                urlExtension.compareToIgnoreCase("doc") != 0 &&
                urlExtension.compareToIgnoreCase("docx") != 0
                ) {
            String altContent = "";
            content = ArticleExtractor.INSTANCE.getText(url);
            if (this.wordCount(content) < 15)
                altContent = DefaultExtractor.INSTANCE.getText(url);
            if (altContent.length() > content.length()) {
                content = altContent;
            }
        }
        return content;
    }

    private int wordCount(String content) {
        String[] arr = content.split(" ");
        int length = arr.length;
        return length;
    }

}
