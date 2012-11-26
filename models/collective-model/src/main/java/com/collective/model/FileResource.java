package com.collective.model;

import java.io.File;
import java.util.UUID;

/**
 * A resource made by a file.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FileResource extends Resource {

    private File file;

    private String description;

    public  FileResource(UUID id) {
        super(id);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FileResource{" +
                "file=" + file +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}