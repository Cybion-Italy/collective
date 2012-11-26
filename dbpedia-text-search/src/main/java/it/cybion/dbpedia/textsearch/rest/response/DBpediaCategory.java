package it.cybion.dbpedia.textsearch.rest.response;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Category")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBpediaCategory {

	@XmlElement(name = "Label")
	private String label;
	@XmlElement(name = "URI")
	private URI uri;
	
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return "DBpediaCategory [label=" + label + ", uri=" + uri + "]";
	}
}
