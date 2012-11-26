package com.collective.activity.storage;

import com.collective.activity.storage.model.DomainFixtures;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.objects.PersonObject;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.configuration.ConfigurationManager;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static com.collective.activity.model.TopicObject.makeTopic;
import static com.collective.activity.model.WebResourceEnhancedObject.makeWebResourceEnhanced;
import static org.apache.abdera2.activities.model.Activity.makeActivity;
import static org.apache.abdera2.activities.model.Collection.makeCollection;
import static org.apache.abdera2.activities.model.Verb.SAVE;
import static org.apache.abdera2.activities.model.objects.PersonObject.makePerson;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DefaultActivityStoreImplTestCase {

    private ActivityStore activityStore;

    private KVStore kvStore;

    private final static String CONFIG_FILE =
            "com/collective/activity/storage/configuration/kvs-storage-configuration.xml";

    private static final Logger logger =
            Logger.getLogger(DefaultActivityStoreImplTestCase.class);
    private List<Long> oneAtATimeInsertionTimes = new ArrayList<Long>();
    private List<Long> bulkInsertionTimes = new ArrayList<Long>();

    @BeforeClass
    public void setUp() {
        Properties properties = ConfigurationManager.getInstance(CONFIG_FILE)
                .getKVStoreConfiguration()
                .getProperties();
        kvStore = new MyBatisKVStore(properties, new SerializationManager());
        this.activityStore = new DefaultActivityStoreImpl(kvStore);
    }

    @AfterClass
    public void tearDown() {
        this.activityStore = null;
        this.oneAtATimeInsertionTimes = null;
        this.bulkInsertionTimes = null;
    }
    
    @Test
    public void shouldTestObjectConstruction() {
        final IO io = IO.get();
        //store
        String userId = "http://collective.com/test/userId/" + UUID.randomUUID();
        final PersonObject examplePerson = makePerson().id(userId).get();

        List<ASObject> topics = new ArrayList<ASObject>();
        topics.add(makeTopic("http://dbpedia.org/first-topic").get());
        topics.add(makeTopic("http://dbpedia.org/second-topic").get());

        Activity savedResourceActivity = makeActivity()
                .actor(examplePerson)
                .verb(SAVE)
                .object(makeWebResourceEnhanced("http://www.test-page.com")
                        .collectiveUrl("http://collective.com/internal/resource/1")
                        .id("res-id-4352")
                        .topics(makeCollection(topics)).get())
                .published(new DateTime())
                .id("activity-id-35236")
                .get();

        savedResourceActivity.writeTo(io, System.out);
    }

    @Test
    public void shouldTestCRD() throws ActivityStoreException {

        final IO io = IO.get();
        //store
        String userId = "http://collective.com/test/userId/" + UUID.randomUUID();
        final PersonObject examplePerson = makePerson().id(userId).get();

        List<ASObject> topics = new ArrayList<ASObject>();
        topics.add(makeTopic("http://dbpedia.org/first-topic").get());
        topics.add(makeTopic("http://dbpedia.org/second-topic").get());

        Activity storedMakeFriendActivity = makeActivity()
                .actor(examplePerson)
                .verb(SAVE)
                .object(makeWebResourceEnhanced("http://www.test-page.com")
                        .collectiveUrl("http://collective.com/internal/resource/1")
                        .id("res-id-4352")
                        .topics(makeCollection(topics)).get())
                .published(new DateTime())
                .id("activity-id-35236")
                .get();

        this.activityStore.storeActorActivity(userId, storedMakeFriendActivity);

        //GET
        Activity retrievedActivity = this.activityStore.getActorActivity(userId,
                storedMakeFriendActivity.getId());

        //check equals
        Assert.assertEquals(retrievedActivity.getId(), storedMakeFriendActivity.getId());
        Assert.assertEquals(retrievedActivity.getActor().getId(),
                     storedMakeFriendActivity.getActor().getId());
        Assert.assertEquals(retrievedActivity.getVerb().toString(),
                     storedMakeFriendActivity.getVerb().toString());
        Assert.assertEquals(retrievedActivity.getObject().getDisplayName(),
                     storedMakeFriendActivity.getObject().getDisplayName());
        Assert.assertEquals(retrievedActivity.getPublished(),
                storedMakeFriendActivity.getPublished());

        //GET range
        long start = new DateTime().minusDays(5).getMillis();
        long end   = new DateTime().getMillis();

        List<Activity> retrievedActivities =
                this.activityStore.getActorActivities(userId, start, end);
        Activity retrievedFirstActivity = retrievedActivities.get(0);

        Assert.assertEquals(retrievedActivities.size(), 1);
        Assert.assertEquals(retrievedFirstActivity.getId(),
                storedMakeFriendActivity.getId());

        //DELETE
        this.activityStore.deleteActorActivities(userId);

        //GET after delete
        List<Activity> emptyActivities =
                this.activityStore.getActorActivities(userId, start, end);
        Assert.assertEquals(emptyActivities.size(), 0);
    }

    @Test
    public void shouldTestMultipleActivities() throws ActivityStoreException {
        //used for a basic load-testing. until around 4700 Activities all works,
        //after it slows and it was stopped manually
        int maxActivities = 10;
        List<Activity> activitiesToSave = new ArrayList<Activity>();

        for (int i = 0; i < maxActivities; i++) {
            Activity activity = DomainFixtures.getActivity(i);
            activitiesToSave.add(activity);            
        }

        String userId = "userid-" + UUID.randomUUID();
        //save
        for (Activity activity : activitiesToSave) {
            long beforeInsert = System.nanoTime();
            this.activityStore.storeActorActivity(userId, activity);
            long afterInsert  = System.nanoTime();
//            logger.info("stored activity: '" + activity.getId() + "'");
            saveInsertionTime(beforeInsert, afterInsert, oneAtATimeInsertionTimes);
        }

        //GET
        List<Activity> retrievedActivities =
                this.activityStore.getAllActorActivities(userId);
        
        Assert.assertEquals(retrievedActivities.size(), activitiesToSave.size());
        Assert.assertEquals(retrievedActivities.get(0).getPublished(),
                               activitiesToSave.get(0).getPublished());

        //DELETE
        this.activityStore.deleteActorActivities(userId);
        long averageInsertionTimeMsec = calculateAverageInsertionTime(
                oneAtATimeInsertionTimes) / 1000000;
        logger.info("average insertion time: '" + averageInsertionTimeMsec
                            + "' millisec over '" + maxActivities + "' insertions");
    }

    private long calculateAverageInsertionTime(List<Long> insertionTimes)
    {
        long acc = 0L;
        for (long insertionTime : insertionTimes) {
            acc += insertionTime;
        }

        return acc / insertionTimes.size();
    }

    private void saveInsertionTime(long beforeInsert, long afterInsert, List<Long> insertionTimes)
    {
        insertionTimes.add(afterInsert - beforeInsert);
    }

    @Test
    public void shouldSaveMultipleActivitiesAtOnce() throws ActivityStoreException {

        //used as load test: until 10000 it works
        int maxActivities = 1000;
        List<Activity> activitiesToSave = new ArrayList<Activity>();

        for (int i = 0; i < maxActivities; i++) {
            Activity activity = DomainFixtures.getActivity(i);
            activitiesToSave.add(activity);
        }

        String userId = "userid-" + UUID.randomUUID();

        //save all activities at once
        long beforeInsert = System.nanoTime();
        this.activityStore.storeActorActivities(userId, activitiesToSave);
        long afterInsert = System.nanoTime();
        saveInsertionTime(beforeInsert, afterInsert, bulkInsertionTimes);
//        logger.info("stored '" + maxActivities + "' activities");

        try {
            //GET
            List<Activity> retrievedActivities =
                    this.activityStore.getAllActorActivities(userId);

            Assert.assertEquals(retrievedActivities.size(), activitiesToSave.size());
            Assert.assertEquals(retrievedActivities.get(0).getPublished(),
                    activitiesToSave.get(0).getPublished());
        } finally {
            //DELETE
            this.activityStore.deleteActorActivities(userId);
        }
        long averageInsertionTimeMsec = calculateAverageInsertionTime(
                bulkInsertionTimes) / 1000000;
        logger.info("average insertion time: '" + averageInsertionTimeMsec
                            + "' millisec over '" + maxActivities + "' bulk insertions");

    }
}
