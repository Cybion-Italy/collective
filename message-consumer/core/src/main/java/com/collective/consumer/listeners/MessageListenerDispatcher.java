package com.collective.consumer.listeners;


import com.collective.consumer.deserializers.MessageBeanDeserializer;
import com.collective.messages.persistence.configuration.MessagesPersistenceConfiguration;
import com.collective.messages.persistence.dao.MessageDao;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import javax.jms.*;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class MessageListenerDispatcher implements MessageListener, ExceptionListener {

    private final static Logger logger = Logger.getLogger(MessageListenerDispatcher.class);
    private MessageBeanDeserializer messageDeserializer;
    private MessageDao messageDao;

    public MessageListenerDispatcher(MessagesPersistenceConfiguration messagesPersistenceConfiguration) {
        messageDeserializer = new MessageBeanDeserializer();
        messageDao = new MessageDao(messagesPersistenceConfiguration.getProperties());
    }

    public void onException(JMSException e) {
        logger.debug("JMS Exception occured.  Shutting down client.");
        System.exit(1);
    }

    public void onMessage(Message message) {
        logger.debug("onMessage method");
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                logger.debug("Received plain text message: " + textMessage.getText());
                String jsonMessage = textMessage.getText();
                //TODO rename class in messages-persistence to prevent ambiguity?
                com.collective.messages.persistence.model.Message messageBean =
                        messageDeserializer.deserializeMessageBean(jsonMessage);
                logger.debug("deserialized to messageBean: " + messageBean);

                //set current time
                messageBean.setTime(new DateTime());

                //store on database
                messageDao.insertMessage(messageBean);
                logger.debug("end of processing phase for message: " + messageBean.toString());
            } catch (JMSException ex) {
                //TODO manage error
                final String eMessage = "Error reading message. ";
                logger.error(eMessage + ex.getMessage());
            }
        } else {
            logger.debug("Received other type of message, not an instance of TextMessage: " + message.toString());
        }
    }
}

