package com.collective.resources.utils;

import com.collective.dynamicprofile.model.DynamicUserProfile;
import com.collective.dynamicprofile.model.Interest;
import com.collective.model.persistence.Source;
import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.resources.UserRecommendation;
import com.collective.resources.gson.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class GsonSerializationTestCase
{

    private Gson gson;

    private Gson customGson;

    private static final Logger logger = Logger.getLogger(GsonSerializationTestCase.class);

    @BeforeTest
    public void setUp()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new JodaDateTimeSerializer())
                .registerTypeAdapter(DateTime.class, new JodaDateTimeDeserializer())
                .registerTypeAdapter(Interest.class, new InterestSerializer())
                .registerTypeAdapter(Interest.class, new InterestDeserializer())
                .registerTypeAdapter(Interval.class, new IntervalSerializer())
                .registerTypeAdapter(Interval.class, new IntervalDeserializer())
                .excludeFieldsWithoutExposeAnnotation();
        this.gson = gsonBuilder.create();
    }

    @AfterTest
    public void tearDown()
    {
        this.gson = null;
    }

    @Test
    public void testJsonGson() throws MalformedURLException, URISyntaxException
    {

        WebResource webResource = new WebResource(
                15,
                "a test web resource",
                "just a test description for this web resource",
                new DateTime(),
                new DateTime(),
                new URL("http://www.fake.com/test"),
                "<body>fake html body</body>",
                "fake html body",
                false
        );

        SourceRss sourceRss = new SourceRss(
                67,
                "IT",
                "fake category",
                "word",
                new URL("http://fakerss.com/rss"),
                new Source(),
                new DateTime(),
                true
        );

        webResource.setSourceRss(sourceRss);
        List<URI> topics = new ArrayList<URI>();
        topics.add(new URI("http://dbpedia.org/resource/fake1"));
        topics.add(new URI("http://dbpedia.org/resource/fake2"));
        WebResourceEnhanced webResourceEnhanced = new WebResourceEnhanced(webResource, topics);
        String jsonWebResourceEnhanced = gson.toJson(webResourceEnhanced);

        logger.debug("json string: \n" + jsonWebResourceEnhanced);

        WebResourceEnhanced deserializedWebResourceEnanched =
                gson.fromJson(jsonWebResourceEnhanced, WebResourceEnhanced.class);
        Assert.assertEquals(webResourceEnhanced, deserializedWebResourceEnanched);


        /* sublist micro test */
        List<WebResourceEnhanced> list = new ArrayList<WebResourceEnhanced>();
        WebResourceEnhanced wr = new WebResourceEnhanced();
        list.add(wr);
        List<WebResourceEnhanced> otherList = list.subList(0, 1);
        logger.debug("original: " + list.getClass().toString());
        logger.debug("subListed: " + otherList.getClass().toString());
    }

    @Test
    public void testSpecificObjectSerialization() throws MalformedURLException, URISyntaxException
    {

        WebResource webResource = new WebResource(
                15,
                "a test web resource",
                "Adafruit's \"Internet of Things Camera\" is a neat mashup of existing Arduino components into a versatile remote monitoring camera. The key here is in the word remote - a capability that's granted by the inclusion of a first-generation Eye-Fi card, which is an SD card with built in Wi-Fi, that can upload images to your computer or other device, or better yet to a variety of photo-sharing websites such as Flickr... \n" +
                        "Continue Reading Build your own motion-triggered &quot;Internet of Things&quot; cameraSection: Digital CamerasTags: Arduino,\n" +
                        " DIY,\n" +
                        " Internet of Things,\n" +
                        " Surveillance\n" +
                        "\n" +
                        "Related Articles:\n" +
                        "\n" +
                        "\tUS$3,000 bounty claimed for open source Kinect drivers\n" +
                        "\tUS$2,000 bounty put on open source drivers for Microsoft's Kinect\n" +
                        "\tFLORA - a platform for your wearable DIY electronics projects\n" +
                        "\tEye-Fi launches wireless memory card for pros \n" +
                        "\tNokia Remote Camera\n" +
                        "\tThe MINOX Leica M3 - a bonsai Digital SLR with 4.0 Megapixels! \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "    \n" +
                        "" +
                        "containing some &quot;specific quotes&quot;",
                new DateTime(),
                new DateTime(),
                new URL("http://www.fake.com/test"),
                "<body>fake html body</body>",
                "fake html body",
                false
        );

        SourceRss sourceRss = new SourceRss(
                67,
                "IT",
                "fake category",
                "word",
                new URL("http://fakerss.com/rss"),
                new Source(),
                new DateTime(),
                true
        );

        webResource.setSourceRss(sourceRss);
        List<URI> topics = new ArrayList<URI>();
        topics.add(new URI("http://dbpedia.org/resource/fake1"));
        topics.add(new URI("http://dbpedia.org/resource/fake2"));
        WebResourceEnhanced webResourceEnhanced = new WebResourceEnhanced(webResource, topics);
        logger.debug("webResourceEnhanced: " + webResourceEnhanced.toString());

        String jsonWebResourceEnhanced = gson.toJson(webResourceEnhanced);
        logger.debug("json string: \n" + jsonWebResourceEnhanced);

        WebResourceEnhanced deserializedWebResourceEnanched =
                gson.fromJson(jsonWebResourceEnhanced, WebResourceEnhanced.class);
        Assert.assertEquals(webResourceEnhanced, deserializedWebResourceEnanched);
    }

    @Test
    public void shouldSerializeDynamicUserProfile() throws URISyntaxException
    {
        DateTime startDate = new DateTime().minusDays(30);
        DateTime endDate = new DateTime().minusDays(7);
        DateTime lastUpdatedAt = new DateTime();
        int interestsAmount = 2;
        DynamicUserProfile expectedUp = DomainFixtures.getDynamicUserProfile(
                UserRecommendation.SHORT_TERM_USER_PROFILE_BASE_URI +
                        "872892",
                startDate,
                endDate,
                lastUpdatedAt,
                interestsAmount);

        String jsonUp = gson.toJson(expectedUp);
        DynamicUserProfile deserializedDup = gson.fromJson(jsonUp, DynamicUserProfile.class);
        Assert.assertEquals(deserializedDup, expectedUp);
    }

}
