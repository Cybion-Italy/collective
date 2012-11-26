package com.collective.messages.persistence.model;

import org.joda.time.DateTime;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class Message {

    private Long id;

    private String type;

    private String action;

    private String object;

    private DateTime time;

    private boolean analyzed;

    public boolean isAnalyzed() {
        return analyzed;
    }

    public void setAnalyzed(boolean analyzed) {
        this.analyzed = analyzed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public String toString() {
        return "Message{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", action='" + action + '\'' +
                ", object='" + object + '\'' +
                ", time=" + time +
                ", analyzed=" + analyzed +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;

        return true;
    }

    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
