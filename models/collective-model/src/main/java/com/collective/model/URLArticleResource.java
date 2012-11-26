package com.collective.model;

import java.net.URL;
import java.util.UUID;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
/* should it be the same as the url saved on database */
public class URLArticleResource extends Resource {
	
	private URL url;

	private String summary;

	protected URLArticleResource(UUID id) {
		super(id);
	}
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return "URLArticleResource{" +
				"url=" + url + ", " +
				"summary=" + summary + "} " + super.toString();
	}

}
