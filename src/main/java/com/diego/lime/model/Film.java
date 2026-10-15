package com.diego.lime.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Film{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int releaseYear;
    private String platform;
    private String Genre;

    protected Film(){}

    public Film(String name, int year, String app, String type){
        this.name = name;
        this.releaseYear = year;
        this.platform = app;
        this.Genre = type;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getPlatform() {
        return platform;
    }

    public String getGenre() {
        return Genre;
    }

}
