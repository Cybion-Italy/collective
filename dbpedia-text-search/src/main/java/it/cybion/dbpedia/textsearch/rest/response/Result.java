package it.cybion.dbpedia.textsearch.rest.response;

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Result")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result {
	
	@XmlElement(name = "Label")
	private String label;
	@XmlElement(name = "URI")
	private URI uri;
	@XmlElement(name = "Description")
	private String description;
	@XmlElementWrapper(name = "Classes")
	@XmlElement(name = "Class")
	private List<DBpediaClass> classes;
	@XmlElementWrapper(name = "Categories")
	@XmlElement(name = "Category")
	private List<DBpediaCategory>categories;
	@XmlElement(name = "Templates")
	private List<String> templates;
	@XmlElement(name = "Redirects")
	private List<String> redirects;
	@XmlElement(name = "Refcount")
	private int refCount;
	
	public Result() {}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<DBpediaClass> getClasses() {
		return classes;
	}
	public void setClasses(List<DBpediaClass> classes) {
		this.classes = classes;
	}
	public List<DBpediaCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<DBpediaCategory> categories) {
		this.categories = categories;
	}
	public int getRefCount() {
		return refCount;
	}
	public void setRefCount(int refCount) {
		this.refCount = refCount;
	}
	@Override
	public String toString() {
		return "Result [label=" + label + ", uri=" + uri + ", description="
				+ description + ", classes=" + classes + ", categories="
				+ categories + ", refCount=" + refCount + "]";
	}
}
