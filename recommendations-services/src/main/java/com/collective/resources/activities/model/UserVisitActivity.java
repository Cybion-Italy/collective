package com.collective.resources.activities.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class UserVisitActivity extends BaseActivity
{

    @Expose
    private List<String> topics = new ArrayList<String>();

    @Expose
    private String content;

    @Expose
    private String sourceUrl;

    public UserVisitActivity()
    {
        super();
    }

    public UserVisitActivity(String activityType, List<String> topics, String content, String sourceUrl)
    {
        super(activityType);
        this.topics = topics;
        this.content = content;
        this.sourceUrl = sourceUrl;
    }

    public List<String> getTopics()
    {
        return topics;
    }

    public void setTopics(List<String> topics)
    {
        this.topics = topics;
    }

    public void addTopic(String topic)
    {
        this.topics.add(topic);
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getSourceUrl()
    {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl)
    {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String toString()
    {
        return "UserVisitActivity{" +
                "topics=" + topics +
                ", content='" + content + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                "} " + super.toString();
    }
}
