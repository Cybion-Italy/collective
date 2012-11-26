package com.collective.document.analyzer.domain;

import it.cybion.proconsult.domain.Fonte;
import it.cybion.proconsult.domain.FonteRss;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

import java.net.URI;
import java.net.URISyntaxException;

@Namespace("http://www.cybion.it/proconsult/")
@RdfType("fonte_rss")
public class FonteRssMapped {
	
	private transient static Logger logger = Logger.getLogger(FonteRssMapped.class);

	@Id
	private Integer id;
	private String lingua;
	private String parola;
	@RdfProperty("http://www.cybion.it/webpage")
	private URI url;
	private Fonte fonte;
	//TODO: convert to something meaningful in 3store to read in sparql queries
	private transient DateTime dataUltimaEstrazione;
	
	public FonteRssMapped(FonteRss fonteRss) {
		id = fonteRss.getId();
		lingua = fonteRss.getLingua();
		parola = fonteRss.getParola();
		try {
			url = new URI(fonteRss.getUrl());
		} catch (URISyntaxException e) {
			logger.error("error while translating url to rdf: wrong uri " + fonteRss.getUrl());
		}
		fonte = fonteRss.getFonte();
		dataUltimaEstrazione = fonteRss.getDataUltimaEstrazione();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLingua() {
		return lingua;
	}

	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	public String getParola() {
		return parola;
	}

	public void setParola(String parola) {
		this.parola = parola;
	}

	public Fonte getFonte() {
		return fonte;
	}

	public void setFonte(Fonte fonte) {
		this.fonte = fonte;
	}

	public DateTime getDataUltimaEstrazione() {
		return dataUltimaEstrazione;
	}

	public void setDataUltimaEstrazione(DateTime dataUltimaEstrazione) {
		this.dataUltimaEstrazione = dataUltimaEstrazione;
	}

	public URI getUrl() {
		return url;
	}

	public void setUrl(URI url) {
		this.url = url;
	}

}
