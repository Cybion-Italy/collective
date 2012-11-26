package com.collective.model;

import java.util.UUID;

/**
 * A free-text human readable resource.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FreeTextResource extends Resource {

    private String text;

    public FreeTextResource(UUID id) {
        super(id);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "FreeTextResource{" +
                "text='" + text + '\'' +
                "} " + super.toString();
    }
}
