package com.diego.lime.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "films")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer releaseYear;
    private String platform;
    @Column(unique = true)
    private Long tmdbId;
    @Column(length = 2000)
    private String overview;
    private String posterPath;

    //TODO Create a Dto for the genres, as it is now we're adding 1 query on each film
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "film_genres",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    protected Film() {
    }

    public Film(String name, Integer releaseYear, String platform, Long tmdbId, String overview, String posterPath) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.platform = platform;
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

    public Long getTmdbId() { return tmdbId; }

    public String getOverview() { return overview; }

    public String getPosterPath() { return posterPath; }

    public Set<Genre> getGenres() {
        return genres;
    }
    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
