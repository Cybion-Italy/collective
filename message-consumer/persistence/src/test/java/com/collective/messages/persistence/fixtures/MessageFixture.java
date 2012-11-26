package com.collective.messages.persistence.fixtures;

import com.collective.messages.persistence.model.Message;
import org.joda.time.DateTime;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class MessageFixture {

    public static Message getMessage() {
        Message m = new Message();
        m.setType("type");
        m.setAction("action");
        m.setObject("json object");
        m.setTime(new DateTime());
        m.setAnalyzed(false);
        return m;
    }

}
