package com.collective.model.persistence;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.net.URL;

/* Expose annotation is used by Gson builder when serializing objects, so that only annotated
* fields will be serialized */

public class WebResource implements Serializable {

    // TODO: id could be changed to Long
    @Expose
    private Integer id;

    @Expose
    @SerializedName("title")
    private String titolo;

    @Expose
    @SerializedName("description")
    private String descrizione;

    @Expose
    @SerializedName("extractionDate")
    private DateTime dataEstrazione;

    @Expose
    @SerializedName("publicationDate")
    private DateTime dataPubblicazione;

    @Expose
    private URL url;

    @SerializedName("htmlContent")
    private String contenutoHtml;

    @SerializedName("textContent")
    private String contenutoTesto;

    private boolean isAnalyzed;
    
    private SourceRss sourceRss;

    public WebResource() {}

    public WebResource(WebResource webResource) {
        this(
                webResource.getId(),
                webResource.getTitolo(),
                webResource.getDescrizione(),
                webResource.getDataEstrazione(),
                webResource.getDataPubblicazione(), 
                webResource.getUrl(),
                webResource.getContenutoHtml(),
                webResource.getContenutoTesto(),
                webResource.isAnalyzed()
        );
    }

    public WebResource(
            Integer id,
            String titolo,
            String descrizione,
            DateTime dataEstrazione,
            DateTime dataPubblicazione,
            URL url,
            String contenutoHtml, 
            String contenutoTesto,
            boolean analyzed) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataEstrazione = dataEstrazione;
        this.dataPubblicazione = dataPubblicazione;
        this.url = url;
        this.contenutoHtml = contenutoHtml;
        this.contenutoTesto = contenutoTesto;
        isAnalyzed = analyzed;
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

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
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

    public SourceRss getSourceRss() {
        return sourceRss;
    }

    public void setSourceRss(SourceRss sourceRss) {
        this.sourceRss = sourceRss;
    }

    public boolean isAnalyzed() {
        return isAnalyzed;
    }

    public void setAnalyzed(boolean isAnalyzed) {
        this.isAnalyzed = isAnalyzed;
    }

    @Override
    public String toString() {
        return "WebResource [id=" + id + ", titolo=" + titolo + ", descrizione="
                + descrizione + ", dataEstrazione=" + dataEstrazione
                + ", dataPubblicazione=" + dataPubblicazione + ", url=" + url
                + ", contenutoHtml=" + contenutoHtml + ", contenutoTesto="
                + contenutoTesto + ", isAnalyzed=" + isAnalyzed + ", sourceRss="
                + sourceRss + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebResource that = (WebResource) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
