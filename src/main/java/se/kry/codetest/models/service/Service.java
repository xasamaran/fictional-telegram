package se.kry.codetest.models.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {

    private String name;

    private String url;

    private LocalDate creationDate;

    private Boolean alive;

    @JsonCreator
    public Service(@JsonProperty("name") String name,
                   @JsonProperty("url") String url) {
        this.name = name;
        this.url = url;
        this.creationDate = LocalDate.now();
        this.alive = false;
    }

    public Service(String name, String url, LocalDate creationDate, Boolean alive) {
        this.name = name;
        this.url = url;
        this.creationDate = creationDate;
        this.alive = alive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
