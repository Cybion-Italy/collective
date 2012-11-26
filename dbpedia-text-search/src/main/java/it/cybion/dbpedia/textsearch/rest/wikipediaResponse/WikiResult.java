package it.cybion.dbpedia.textsearch.rest.wikipediaResponse;

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "p")
@XmlAccessorType(XmlAccessType.FIELD)
public class WikiResult {
	
	@XmlAttribute(name = "title")
	private String title;
	
	public WikiResult() {}
	
	public String getTitle() {
		return title;
	}
	@Override
	public String toString() {
		return "Result [title=" + title +"]";
	}
}
