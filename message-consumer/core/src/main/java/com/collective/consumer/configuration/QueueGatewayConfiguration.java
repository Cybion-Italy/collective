package com.collective.consumer.configuration;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class QueueGatewayConfiguration {

    private String host;
    private String port;
    private String queueName;

    public QueueGatewayConfiguration(String host, String port, String queueName) {
        this.host = host;
        this.port = port;
        this.queueName = queueName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
