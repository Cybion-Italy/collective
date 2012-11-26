package com.collective.importer.indexer;

import com.collective.importer.Line;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MySqlImporterTestCase {

    private MySqlImporter mySqlImporter;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        mySqlImporter = new MySqlImporter();
    }

    @Test
    public void testIndexRetrieveAndDelete()
            throws MySqlImporterException, MalformedURLException {
        Line expected = new Line(
                new URL("http://dbpedia.org/resource/Bassiano"),
                "Bassiano"
        );
        final long owner = 563653L;
        expected.setOwner(owner);
        mySqlImporter.storeLine(expected);
        Assert.assertEquals(mySqlImporter.getLine(expected.getUrl()), expected);
        Assert.assertEquals(mySqlImporter.suggest("Bassiano", owner, true).size(), 1);
        Assert.assertEquals(mySqlImporter.suggest("Bassiano", owner, true).get(0).getOwner(), 563653L);
        mySqlImporter.deleteLine(expected.getUrl());
        Assert.assertNull(mySqlImporter.getLine(expected.getUrl()));
    }

}
