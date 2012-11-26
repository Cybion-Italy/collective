package it.cybion.dbpedia.textsearch.rest.wikipediaResponse;

import it.cybion.dbpedia.textsearch.rest.wikipediaResponse.WikiResult;

import java.net.URI;
import java.util.List;

import javax.jws.WebResult;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "search")
@XmlAccessorType(XmlAccessType.FIELD)
public class WikiSearch {
	
	@XmlElement(name = "p", required=false)
	private List<WikiResult> pResult;
	
	public WikiSearch() {}
	
	public List<WikiResult> getPResults() {
		return pResult;
	}
	@Override
	public String toString() {
		return "Search [pResult=" + pResult +"]";
	}
}
