package com.diego.lime.model;

import jakarta.persistence.*;

@Entity
@Table(name = "films")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer releaseYear;
    private String platform;
    private String genre;
    @Column(unique = true)
    private Long tmdbId;
    @Column(length = 2000)
    private String overview;
    private String posterPath;

    protected Film() {
    }

    public Film(String name, Integer releaseYear, String platform, String genre, Long tmdbId, String overview, String posterPath) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.platform = platform;
        this.genre = genre;
        this.tmdbId = tmdbId;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public String getPlatform() {
        return platform;
    }

    public String getGenre() {
        return genre;
    }

    public Long getTmdbId() { return tmdbId; }

    public String getOverview() { return overview; }

    public String getPosterPath() { return posterPath; }
}
