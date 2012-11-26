package com.collective.downloader.rss.model;


import com.collective.model.persistence.WebResource;

import java.util.Comparator;

/**
 * Compares two {@link com.collective.model.persistence.WebResource} by the downloader date.
 */
public class CompareWebResourcesByDate implements Comparator<WebResource> {

	public int compare(WebResource u1, WebResource u2) {
		return u1.getDataEstrazione().compareTo(u2.getDataEstrazione());
	}
	
}
