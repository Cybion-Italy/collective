package com.collective.document.analyzer.domain;

import it.cybion.proconsult.domain.Url;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

/* maps it.cybion.proconsult.domain.Url object to the triple store. 
 * rebuilt here to prevent annotations on the persistence project */
@Namespace("http://www.cybion.it/proconsult/")
@RdfType("url")
public class UrlMapped {
	
    private transient static Logger logger = Logger.getLogger(UrlMapped.class);
	
	/* TODO: id could be changed to Long */
	@Id
	private Integer id;
	
	@RdfProperty("http://purl.org/dc/terms/title")
	private String titolo;
	
	@RdfProperty("http://purl.org/dc/terms/abstract")
	private String descrizione;
	
	private transient DateTime dataEstrazione;
	
	private transient DateTime dataPubblicazione;
	
	@RdfProperty("http://www.cybion.it/proconsult/url#webpage")
	private URI url;
	
	private transient String contenutoHtml;
	
	private transient String contenutoTesto;
	
//	private String tipoContenuto;
	private transient boolean isAnalyzed;
	
	@RdfProperty("http://www.cybion.it/proconsult/url#hasFonteRss")
	private FonteRssMapped fonteRssMapped;
	
	@RdfProperty("http://purl.org/dc/terms/subject")
	Collection<URI> topics;
	
	public UrlMapped(Url document, List<URI> annotations) {
		this.id = document.getId();
		this.titolo = document.getTitolo();
		this.descrizione = document.getDescrizione();
		this.dataEstrazione = document.getDataEstrazione();
		this.dataPubblicazione = document.getDataPubblicazione();
		try {
			this.url = new URI(document.getUrl());
		} catch (URISyntaxException e) {
			logger.error("error while translating url to rdf: wrong uri " + document.getUrl());
		}
		//do not save additional text in virtuoso?
//		this.contenutoHtml = document.getContenutoHtml();
//		this.contenutoTesto = document.getContenutoTesto();
		this.isAnalyzed = document.isAnalyzed();
		this.fonteRssMapped = new FonteRssMapped(document.getFonteRss());
		this.topics = annotations;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public DateTime getDataEstrazione() {
		return dataEstrazione;
	}

	public void setDataEstrazione(DateTime dataEstrazione) {
		this.dataEstrazione = dataEstrazione;
	}

	public DateTime getDataPubblicazione() {
		return dataPubblicazione;
	}

	public void setDataPubblicazione(DateTime dataPubblicazione) {
		this.dataPubblicazione = dataPubblicazione;
	}

	public URI getUrl() {
		return url;
	}

	public void setUrl(URI url) {
		this.url = url;
	}

	public String getContenutoHtml() {
		return contenutoHtml;
	}

	public void setContenutoHtml(String contenutoHtml) {
		this.contenutoHtml = contenutoHtml;
	}

	public String getContenutoTesto() {
		return contenutoTesto;
	}

	public void setContenutoTesto(String contenutoTesto) {
		this.contenutoTesto = contenutoTesto;
	}

	public boolean isAnalyzed() {
		return isAnalyzed;
	}

	public void setAnalyzed(boolean isAnalyzed) {
		this.isAnalyzed = isAnalyzed;
	}

	public FonteRssMapped getFonteRssMapped() {
		return fonteRssMapped;
	}

	public void setFonteRssMapped(FonteRssMapped fonteRssMapped) {
		this.fonteRssMapped = fonteRssMapped;
	}

	public Collection<URI> getTopics() {
		return topics;
	}

	public void setTopics(Collection<URI> topics) {
		this.topics = topics;
	}

}
