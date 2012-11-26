package com.collective.importer.reader;

import com.collective.importer.Line;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class NQReaderTestCase {

    private static Logger logger = Logger.getLogger(NQReaderTestCase.class);

    private NQReader nqReader;

    @BeforeTest
    public void setUp() {
        File file = new File("src/test/resources/com/collective/importer/reader/test.nq");
        nqReader = new NQReader(file);
    }

    @Test
    public void testParse() throws NQReaderException {
        nqReader.parse();
        Assert.assertTrue(nqReader.hasNextLine());
        int amount = 0;
        while(nqReader.hasNextLine()) {
            Line line = nqReader.nextLine();
            Assert.assertNotNull(line);
            amount++;
            logger.debug("LINE: : " + line);
        }
        Assert.assertEquals(amount, 5);
    }

}
