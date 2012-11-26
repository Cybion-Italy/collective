package com.collective.model.persistence;

import java.net.URL;
import java.util.List;

/**
 * @author Matteo Moci ( moci@cybion.it )
 */
public class Source {

    private Integer id;

    private String nome;

    private URL url;

    private List<SourceRss> sourcesRss;

    public Source() {
        this.id = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public List<SourceRss> getSourcesRss() {
        return sourcesRss;
    }

    public void setSourcesRss(List<SourceRss> sourcesRss) {
        this.sourcesRss = sourcesRss;
    }

    @Override
    public String toString() {
        return "Source [id=" + id + ", nome=" + nome + ", url=" + url
                + ", sourcesRss=" + sourcesRss + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Source source = (Source) o;

        if (!id.equals(source.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
