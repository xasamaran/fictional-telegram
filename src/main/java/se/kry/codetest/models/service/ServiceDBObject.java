package se.kry.codetest.models.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;


public class ServiceDBObject {

    private String name;
    private String url;
    private LocalDate localDate;
    private Integer alive;


    @JsonCreator()
    public ServiceDBObject(@JsonProperty("name") String name,
                   @JsonProperty("url") String url,
                   @JsonProperty("creationDate") Long localDate,
                   @JsonProperty("alive") Integer alive) {
        this.name = name;
        this.url = url;
        this.localDate =  Instant.ofEpochMilli(localDate).atZone(ZoneId.systemDefault()).toLocalDate();;
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

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Integer getAlive() {
        return alive;
    }

    public void setAlive(Integer alive) {
        this.alive = alive;
    }
}
