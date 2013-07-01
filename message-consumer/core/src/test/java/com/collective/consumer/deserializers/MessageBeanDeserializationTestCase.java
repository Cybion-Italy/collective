package com.collective.consumer.deserializers;

import com.collective.consumer.domain.TestFixtures;
import com.collective.messages.persistence.model.Message;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class MessageBeanDeserializationTestCase {

    private MessageBeanDeserializer gsonMessageBeanDeserializer;

    private final static Logger LOGGER = Logger.getLogger(MessageBeanDeserializationTestCase.class);

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
        LOGGER.debug("deserializing message: " + jsonMessage);
        Message messageBean = gsonMessageBeanDeserializer.deserializeMessageBean(jsonMessage);
        LOGGER.debug("into messageBean: " + messageBean);
        assertTrue(messageBean.getAction().equals("update"));
        assertTrue(messageBean.getType().equals("user"));
        assertNotNull(messageBean.getObject());
        assertTrue(messageBean.getObject().equals(TestFixtures.jsonUser));
    }

}
