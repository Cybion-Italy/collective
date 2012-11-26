package com.collective.consumer.deserializers;

import com.collective.consumer.domain.TestFixtures;
import com.collective.messages.persistence.model.Message;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class MessageBeanDeserializationTestCase {

    private MessageBeanDeserializer gsonMessageBeanDeserializer;

    private final static Logger logger = Logger.getLogger(MessageBeanDeserializationTestCase.class);

    @BeforeTest
    public void setUp() {
        this.gsonMessageBeanDeserializer = new MessageBeanDeserializer();
    }

    @AfterTest
    public void tearDown() {
        this.gsonMessageBeanDeserializer = null;
    }

    @Test
    public void shouldDeserializeMessageBean() {
        String jsonMessage = TestFixtures.jsonMessage;
        logger.debug("deserializing message: " + jsonMessage);
        Message messageBean = gsonMessageBeanDeserializer.deserializeMessageBean(jsonMessage);
        logger.debug("into messageBean: " + messageBean);
        Assert.assertTrue(messageBean.getAction().equals("update"));
        Assert.assertTrue(messageBean.getType().equals("user"));
        Assert.assertNotNull(messageBean.getObject());
        Assert.assertTrue(messageBean.getObject().equals(TestFixtures.jsonUser));
    }

}
