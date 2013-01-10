package com.collective.messages.persistence.dao;

import com.collective.messages.persistence.fixtures.MessageFixture;
import com.collective.messages.persistence.model.Message;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class MessageDaoTestCase extends AbstractDaoTestCase {

    private MessageDao messageDao;

    private static final Logger logger = Logger.getLogger(MessageDaoTestCase.class);

    public MessageDaoTestCase() {
        super();
    }

    @BeforeClass
    public void setUp() {
        this.messageDao = new MessageDao(messagesPersistenceConfiguration.getProperties());
    }

    @AfterClass
    public void tearDown() {
        this.messageDao = null;
    }

    @Test
    public void shouldTestBasicCRUD() {
        Message message = MessageFixture.getMessage();
        this.messageDao.insertMessage(message);
        assertNotNull(message.getId());
        logger.debug("inserted message with id: " + message.getId());
        try {
            Message insertedMessage = this.messageDao.selectMessage(message.getId());
            assertEquals(insertedMessage, message);

            insertedMessage.setAnalyzed(!message.isAnalyzed());
            insertedMessage.setAction("different-action");
            insertedMessage.setType("diff-type");
            insertedMessage.setObject("diff-object");
            insertedMessage.setTime(new DateTime());

            this.messageDao.updateMessage(insertedMessage);

            Message updatedMessage = this.messageDao.selectMessage(insertedMessage.getId());

            assertEquals(updatedMessage.getId(), insertedMessage.getId());
            assertEquals(updatedMessage.getAction(), insertedMessage.getAction());
            assertEquals(updatedMessage.getType(), insertedMessage.getType());
            assertEquals(updatedMessage.getTime(), insertedMessage.getTime());
            assertEquals(updatedMessage.getObject(), insertedMessage.getObject());
        } finally {
            this.messageDao.deleteMessage(message.getId());
            Message deletedMessage = this.messageDao.selectMessage(message.getId());
            assertNull(deletedMessage);
            logger.debug("deleted message with id: " + message.getId());
        }
    }

    @Test
    public void shouldTestSelectByExample() {

        //insert 3 fake messages: 2 are the same
        Message m1 = MessageFixture.getMessage();
        Message m2 = MessageFixture.getMessage();
        m2.setTime(m2.getTime().plusMillis(10));
        // and one is different
        Message mDifferent = MessageFixture.getMessage();
        mDifferent.setAction("really-strange-action");
        mDifferent.setType("class-type");
        mDifferent.setTime(mDifferent.getTime().plusMillis(10));

        //add all to a list
        List<Message> messages = new ArrayList<Message>();
        messages.add(m1);
        messages.add(m2);
        messages.add(mDifferent);
        //insert all
        for (Message messageToInsert : messages) {
            this.messageDao.insertMessage(messageToInsert);
            assertNotNull(messageToInsert.getId());
        }

        try {
            Message exampleMessage = m1;

            final int maxItems = 2;
            List<Message> messagesByExampleOrdered =
                    this.messageDao.selectLastMessagesByExample(exampleMessage, maxItems);

            assertTrue(messagesByExampleOrdered.size() <= maxItems);

            DateTime currentDate = new DateTime(0);
            for (Message currentMessage : messagesByExampleOrdered) {
                logger.info("message: " + currentMessage);
                assertEquals(currentMessage.getAction(), exampleMessage.getAction());
                assertEquals(currentMessage.getType(), exampleMessage.getType());
                assertEquals(currentMessage.isAnalyzed(), exampleMessage.isAnalyzed());

                //assert that currentMessage time is after the previously seen time
                assertTrue(currentDate.compareTo(currentMessage.getTime()) <= 0);
                //update the 'maximum'
                currentDate = currentMessage.getTime();
            }
        } finally {
            //delete all
            for (Message messageToDelete : messages) {
                this.messageDao.deleteMessage(messageToDelete.getId());
                assertNull(this.messageDao.selectMessage(messageToDelete.getId()));
            }
        }
    }

    @Test
    public void shouldTestSelectByExampleSpecificTest() {
        Message m1 = MessageFixture.getMessage();
        m1.setAction("update");
        m1.setTime(new DateTime(0));
        Message m2 = MessageFixture.getMessage();
        m2.setAction("create");
        m2.setTime(m2.getTime().plusMillis(10));

        //add all to a list
        List<Message> messages = new ArrayList<Message>();
        messages.add(m1);
        messages.add(m2);

        //insert all
        for (Message messageToInsert : messages) {
            this.messageDao.insertMessage(messageToInsert);
            assertNotNull(messageToInsert.getId());
        }

        try {
            Message exampleMessage = new Message();
            exampleMessage.setAction("update");

            int maxItems = 2;
            List<Message> messagesByExampleOrdered = this.messageDao.selectLastMessagesByExample(exampleMessage, maxItems);

            assertTrue(messagesByExampleOrdered.size() <= maxItems);

            DateTime currentDate = new DateTime(0);
            for (Message currentMessage : messagesByExampleOrdered) {
                logger.info("message: " + currentMessage);
                assertEquals(currentMessage.getAction(), exampleMessage.getAction());
                assertEquals(currentMessage.isAnalyzed(), exampleMessage.isAnalyzed());

                //assert that currentMessage time is after the previously seen time
                assertTrue(currentDate.compareTo(currentMessage.getTime()) <= 0);
                //update the 'maximum'
                currentDate = currentMessage.getTime();
            }
        } finally {
            //delete all
            for (Message messageToDelete : messages) {
                this.messageDao.deleteMessage(messageToDelete.getId());
                assertNull(this.messageDao.selectMessage(messageToDelete.getId()));
            }
        }
    }


    @Test
    public void shouldTestisAnalyzedField() {
        Message m1 = MessageFixture.getMessage();
        m1.setAction("another-uberfake-action");
        m1.setTime(new DateTime(0));
        m1.setAnalyzed(false);

        //insert
        this.messageDao.insertMessage(m1);

        try {
            Message exampleMessage = m1;
            int maxItems = 2;
            //select
            List<Message> listMessages = this.messageDao.selectLastMessagesByExample(exampleMessage, maxItems);

            for (Message message : listMessages) {
                assertEquals(message.getAction(), exampleMessage.getAction());
                assertEquals(message.isAnalyzed(), exampleMessage.isAnalyzed());
            }
        } finally {
            //delete
            this.messageDao.deleteMessage(m1.getId());
            assertNull(this.messageDao.selectMessage(m1.getId()));
        }
    }

    @Test(enabled = false)
    public void shouldFixBugTest() {
        Message m1 = MessageFixture.getMessage();
        m1.setAction("update");
        m1.setType("user");
        m1.setObject(null);
        m1.setAnalyzed(false);

        List<Message> messages = this.messageDao.selectLastMessagesByExample(m1, 10);

        logger.debug("messages: " + messages.size());
        for (Message message : messages) {
            logger.debug("message: " + message.toString());
        }

    }
}
