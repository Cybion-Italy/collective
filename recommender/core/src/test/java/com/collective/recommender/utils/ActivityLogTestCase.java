package com.collective.recommender.utils;

import com.collective.recommender.configuration.ConfigurationManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tv.notube.commons.storage.alog.DefaultActivityLogImpl;
import tv.notube.commons.storage.model.Activity;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.ActivityLogException;
import tv.notube.commons.storage.model.fields.Field;
import tv.notube.commons.storage.model.fields.IntegerField;
import tv.notube.commons.storage.model.fields.StringField;

/**
 * TODO high: move to proper aLog module and activate
 *
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ActivityLogTestCase
{

    private static Logger logger = Logger.getLogger(ActivityLogTestCase.class);

    private final static String CONFIG_FILE = "recommender-configuration.xml";

    private ActivityLog activityLog;

    private static final String OWNER = "test-owner";

    private static final int activitiesAmount = 3;

    @BeforeClass
    public void setUp()
    {
        activityLog = new DefaultActivityLogImpl(
                ConfigurationManager.getInstance(CONFIG_FILE)
                        .getActivityLogConfiguration());

        String dbUrl =
                ConfigurationManager.getInstance(CONFIG_FILE)
                        .getActivityLogConfiguration().getProperty("url");

        Assert.assertTrue(dbUrl.endsWith("collective-alog"));
    }

    @AfterClass
    public void shutDown()
    {
        activityLog = null;
    }

    @Test(enabled = false)
    public void shouldTestActivityLogger() throws ActivityLogException
    {
        DateTime before = new DateTime();
        for (int i = 0; i < activitiesAmount; i++) {
            IntegerField index = new IntegerField("index", i);
            Field fields[] = {index};
            activityLog.log(OWNER, "just a test activity", fields);
        }
        DateTime now = new DateTime();
        Activity activities[] = activityLog.filter(before, now);
        Assert.assertEquals(activitiesAmount, activities.length);

        activities = activityLog.filter(before, now, OWNER);
        Assert.assertEquals(activitiesAmount, activities.length);

        for (Activity activity : activities) {
            Field[] fields = activityLog.getFields(activity.getId());
            Assert.assertEquals(1, fields.length);
        }
        activityLog.delete(OWNER);
        activities = activityLog.filter(before, now);
        Assert.assertEquals(0, activities.length);
    }

    @Test(enabled = true)
    public void shouldTestActivityLoggerWithLongTexts() throws ActivityLogException
    {
        DateTime before = new DateTime();

        String longString = "LongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten" +
                "LongestStringEverWrittenLongestStringEverWritten";

        StringField longText = new StringField("longtext", longString);
        Field activityFields[] = {longText};

        activityLog.log(OWNER, "just a test activity with longest field", activityFields);

        DateTime now = new DateTime();

        Activity activities[] = activityLog.filter(before, now, OWNER);

        for (Activity activity : activities) {
            Field[] fields = activityLog.getFields(activity.getId());
            Assert.assertEquals(1, fields.length);

            for (Field field : fields) {
                Assert.assertEquals(field.getName(), "longtext");
                Assert.assertEquals(field.getValue(), longString);
            }
        }
        activityLog.delete(OWNER);
        activities = activityLog.filter(before, now);
        Assert.assertEquals(0, activities.length);
    }
}
