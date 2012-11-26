package it.cybion.dbpedia.textsearch.rest.response;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ArrayOfResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArrayOfResult {

	@XmlElement(name = "Result")
	private List<Result> results;
	
	public ArrayOfResult() {
		results = new LinkedList<Result>();
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "ArrayOfResult [ results=" + results + "]";
	}
}
