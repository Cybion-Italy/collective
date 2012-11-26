package it.cybion.dbpedia.textsearch.rest.wikipediaResponse;

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "query")
@XmlAccessorType(XmlAccessType.FIELD)
public class WikiQuery {
	
	@XmlElement(name = "search")
	private WikiSearch search;
	
	public WikiQuery() {
		search = new WikiSearch();
	}	
	public WikiSearch getSearch() {
		return search;
	}
	@Override
	public String toString() {
		return "Query [search=" + search +"]";
	}
}
