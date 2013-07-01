package com.collective.consumer;

import com.collective.consumer.configuration.QueueGatewayConfiguration;
import com.collective.consumer.listeners.MessageListenerDispatcher;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;

/**
 * This class starts to listen to queues coming from the configured user queue.
 * In this case, it consumes messages and has a {@link com.collective.consumer.listeners.MessageListenerDispatcher}
 * to manage events of this type.
 *
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class QueueGateway {

    private String host;
    private String port;
    private String queueName;
    private String completeUrl;
    private ConnectionFactory connectionFactory;
    private MessageListenerDispatcher messageListenerDispatcher;

    private static final Logger LOGGER = Logger.getLogger(QueueGateway.class);

    public QueueGateway(QueueGatewayConfiguration queueGatewayConfiguration) {
        host = queueGatewayConfiguration.getHost();
        port = queueGatewayConfiguration.getPort();
        queueName = queueGatewayConfiguration.getQueueName();
        completeUrl = "tcp://" + host + ":" + port;
        /* TODO: refactor as singleton with guice? */
        connectionFactory = new ActiveMQConnectionFactory(completeUrl);
        LOGGER.debug("built user gateway with connection factory");
    }

    public void run() throws QueueGatewayException {
        LOGGER.debug("running user gateway message consumer");
        Connection connection;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (JMSException e) {
            final String errMsg = "connectionFactory failed to start a connection";
            LOGGER.error(errMsg);
            throw new QueueGatewayException(errMsg, e);
        }
        LOGGER.debug("started connection to: " + connection.toString());
        Session session = null;
        try {
            session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(this.queueName);
            LOGGER.debug("created destination on queue: " + this.queueName);
            MessageConsumer consumer = session.createConsumer(destination);
            LOGGER.debug("created consumer");

            //set local message listener: internal business logic goes here
            connection.setExceptionListener(messageListenerDispatcher);
            consumer.setMessageListener(messageListenerDispatcher);

            LOGGER.debug("added consumer and exception listener to connection and consumer");
        } catch (JMSException e) {
            final String errMsg = "failed to create a session and to add a message";
            LOGGER.error(errMsg);
            throw new QueueGatewayException(errMsg, e);
        }
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setMessageListenerDispatcher(MessageListenerDispatcher messageListenerDispatcher) {
        this.messageListenerDispatcher = messageListenerDispatcher;
    }
}
