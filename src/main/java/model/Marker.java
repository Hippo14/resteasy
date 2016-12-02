package model;

import java.io.Serializable;

/**
 * Created by MSI on 2016-12-02.
 */
public class Marker implements Serializable {

    String title;
    String username;
    String description;

    public Marker() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
