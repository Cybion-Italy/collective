package com.collective.profilingline.skos.repository;

import com.collective.profilingline.skos.SkosSubject;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class MyBatisSkosRepositoryTestCase {

    private SkosRepository skosRepository;

    @BeforeTest
    public void setUp() {
        skosRepository = new MyBatisSkosRepository();

    }

    @AfterTest
    public void tearDown() {
        skosRepository = null;
    }

    @Test
    public void testGetSkosByUri() throws URISyntaxException, SkosRepositoryException {
        SkosSubject skosSubject = skosRepository.getSkosByURI(
                new URI("http://dbpedia.org/resource/Category:1987_EPs")
        );
        Assert.assertNotNull(skosSubject);
        Assert.assertEquals(skosSubject, new SkosSubject("1987_EPs", "1987 EPs"));
    }

    @Test
    public void testLookup() throws URISyntaxException, SkosRepositoryException {
        SkosSubject skosSubject = skosRepository.lookup(
                new URI("http://dbpedia.org/resource/Portuguese_Basketball_Cup")
        );
        Assert.assertNotNull(skosSubject);
        Assert.assertEquals(skosSubject, new SkosSubject("Basketball_in_Portugal", "Basketball in Portugal"));

    }

}
