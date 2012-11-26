package com.collective.model.persistence;

import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.net.URL;

/**
 * @author Matteo Moci ( moci@cybion.it )  
 */
public class SourceRss implements Serializable {

    //TODO: refactor url to URL
    private Integer id;

    @SerializedName("language")
    private String lingua;

    @SerializedName("category")
    private String categoria;

    @SerializedName("word")
    private String parola;

    private URL url;

    private Source source;

    @SerializedName("lastExtractionDate")
    private DateTime dataUltimaEstrazione;

    private boolean valid;

    public SourceRss() {}

    public SourceRss(SourceRss sourceRss) {
        this(
                sourceRss.getId(),
                sourceRss.getLingua(),
                sourceRss.getCategoria(),
                sourceRss.getParola(),
                sourceRss.getUrl(),
                sourceRss.getSource(),
                sourceRss.getDataUltimaEstrazione(),
                sourceRss.isValid()
        );
    }

    public SourceRss(
            Integer id,
            String lingua,
            String categoria,
            String parola,
            URL url,
            Source source,
            DateTime dataUltimaEstrazione,
            boolean valid) {
        this.id = id;
        this.lingua = lingua;
        this.categoria = categoria;
        this.parola = parola;
        this.url = url;
        this.source = source;
        this.dataUltimaEstrazione = dataUltimaEstrazione;
        this.valid = valid;
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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public DateTime getDataUltimaEstrazione() {
        return dataUltimaEstrazione;
    }

    public void setDataUltimaEstrazione(DateTime dataUltimaEstrazione) {
        this.dataUltimaEstrazione = dataUltimaEstrazione;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String toString() {
        return "SourceRss{" +
                "id=" + id +
                ", lingua='" + lingua + '\'' +
                ", categoria='" + categoria + '\'' +
                ", parola='" + parola + '\'' +
                ", url='" + url + '\'' +
                ", source=" + source +
                ", dataUltimaEstrazione=" + dataUltimaEstrazione +
                ", valid=" + valid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceRss sourceRss = (SourceRss) o;

        if (id != null ? !id.equals(sourceRss.id) : sourceRss.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
