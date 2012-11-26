package it.cybion.extractor;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class ContentExtractorTestCase
{

    private Logger logger = Logger.getLogger(ContentExtractorTestCase.class);

    //TODO: refactor path file loading of test resources
    @Test
    public void testGetContentFromUrl() throws IOException
    {
        ContentExtractor ce = new ContentExtractor();
        InputStream inputStream = (new URL("file:resources/test_extracted_test")).openStream();
        String testContent = readFromFile(inputStream);
        String content;
        try {
            content = ce.getContentFromUrl(new URL("file:resources/test_extraction.html"));
            logger.info(content);
            Assert.assertTrue(content != "");
            Assert.assertEquals(testContent, content);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.fail("MalformedURLException");
        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
            Assert.fail("BoilerpipeProcessingException");
        }
    }

    @Test
    public void testGetContentFromPDFUrl()
    {
        ContentExtractor ce = new ContentExtractor();
        String content = "";
        try {
            content = ce.getContentFromUrl(new URL("file:resources/test_extraction.pdf"));
            logger.debug("<content>\n" + content + "\n</content>");
            Assert.assertTrue(content.equalsIgnoreCase(""));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.fail("MalformedURLException");
        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
            Assert.fail("BoilerpipeProcessingException");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Generic Exception");
        }
    }

    @Test
    public void testLotsOfAnalysis() throws MalformedURLException, BoilerpipeProcessingException
    {
        //failing url
        //http://feedproxy.google.com/~r/blogspot/advancednano/~3/4MQ5D1GWO4c/novel-materials-could-make-practical.html
        String url = "file:resources/test_extraction_failure.html";
        ContentExtractor ce = new ContentExtractor();
        String content = "";

        //tried with 5000 and no errors
        Long maxTrials = new Long(1);

        Long i = new Long(0);

        while (i.compareTo(maxTrials) == -1) {
            content = ce.getContentFromUrl(new URL(url));
            logger.debug("<content>\n" + content + "\n</content>");
            logger.debug("trial: " + i);
            i = i + 1;
        }
    }

    @Test(enabled = false)
    public void testGetContentFromTEDUrl()
    {
        ContentExtractor ce = new ContentExtractor();
        String content = "";
        try {
            String extractionUrl = "file:resources/test_extraction_ted.html";
            content = ce.getContentFromUrl(new URL(extractionUrl));
//            content = ce.getContentFromUrl(file.toURI().toURL());
            logger.info("<content>\n" + content + "\n</content>");
            //simplified test
            Assert.assertTrue(content.contains("SK-Bratislava: Naval CONSORTIUM"));
            Assert.assertTrue(content.contains("Slovensk republika, Bratislava,"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.fail("MalformedURLException");
        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
            Assert.fail("BoilerpipeProcessingException " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Generic Exception");
        }
    }

    @Test
    public void tempTest() throws IOException
    {
        InputStream inputStream = (new URL("file:resources/test_extracted_test")).openStream();
        logger.info(readFromFile(inputStream));
    }

    private String readFromFile(InputStream inputStream) throws IOException
    {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(
                    new InputStreamReader(inputStream, "ISO-8859-1"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            inputStream.close();
        }
        return writer.toString();
    }
}
