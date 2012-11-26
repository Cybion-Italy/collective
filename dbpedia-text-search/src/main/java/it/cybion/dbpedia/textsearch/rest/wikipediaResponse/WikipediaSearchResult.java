package it.cybion.dbpedia.textsearch.rest.wikipediaResponse;

import it.cybion.dbpedia.textsearch.rest.wikipediaResponse.WikiQuery;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAnyElement;
/*
 * 
 * 
 *
 * <?xml version="1.0"?>
<api>
  <query>
    <searchinfo totalhits="4212" />
    <search>
      <p 
 * 
 */
@XmlRootElement(name = "api")
public class WikipediaSearchResult {

	private WikiQuery query;
	
	public WikipediaSearchResult() {
		query = new WikiQuery();
	}
	
	@XmlElement(name = "query")
	public WikiQuery getQuery() {
		return query;
	}

	public void setQuery(WikiQuery query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return "WikipediaSearchResult [ results=" + query + "]";
	}
}
