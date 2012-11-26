package com.collective.consumer;

import javax.jms.JMSException;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class QueueGatewayException extends Exception {

    public QueueGatewayException(String message, Exception ex) {
        super(message, ex);
    }
}
