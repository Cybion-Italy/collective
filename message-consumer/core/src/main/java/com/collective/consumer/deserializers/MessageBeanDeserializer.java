package com.collective.consumer.deserializers;

import com.collective.messages.persistence.model.Message;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

/**
 * Simple gson class configuration wrapper: it sets up a gson deserializer with a custom configuration
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class MessageBeanDeserializer {

    private final static Logger logger = Logger.getLogger(MessageBeanDeserializer.class);
    private JsonParser parser;

    public MessageBeanDeserializer() {
        parser = new JsonParser();
    }

    public Message deserializeMessageBean(String jsonMessage) {
        JsonObject j = (JsonObject) parser.parse(jsonMessage);
        Message messageBean = new Message();
        messageBean.setAction(j.get("action").getAsString());
        messageBean.setType(j.get("type").getAsString());
        messageBean.setObject(j.get("object").toString());
        return messageBean;
    }
}
