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
    private String genre;

    protected Film(){}

    public Film(String name, int releaseYear, String platform, String genre){
        this.name = name;
        this.releaseYear = releaseYear;
        this.platform = platform;
        this.genre = genre;
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
        return genre;
    }

}
