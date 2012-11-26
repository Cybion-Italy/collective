package com.collective.resources.enrichment;

import com.google.gson.annotations.Expose;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class EnrichmentRequest
{
    @Expose
    private String text;

    @Expose
    private String url;

    public EnrichmentRequest() {

    }

    public EnrichmentRequest(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public String getText()
    {

        return text;
    }

    public void setText(String text)
    {

        this.text = text;
    }

    public String getUrl()
    {

        return url;
    }

    public void setUrl(String url)
    {

        this.url = url;
    }

    @Override
    public boolean equals(Object o)
    {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnrichmentRequest that = (EnrichmentRequest) o;

        if (text != null ? !text.equals(that.text) : that.text != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {

        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {

        return "EnrichmentRequest{" +
                "text='" + text + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
