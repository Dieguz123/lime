package com.diego.lime.tmdb;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;


@Component
public class TmdbClient {

    private final RestClient restClient;

    public TmdbClient(RestClient.Builder builder, TmdbProperties properties) {
        this.restClient = builder
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.token())
                .build();
    }

    public List<TmdbMovie> getPopularSubscriptionMovies( int page ){
        TmdbPage response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/discover/movie")
                        .queryParam("language", "it-IT")
                        .queryParam("watch_region", "IT")
                        .queryParam("with_watch_monetization_types", "flatrate")
                        .queryParam("sort_by", "popularity.desc")
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .body(TmdbPage.class);

        return response != null ? response.results() : List.of();
    }

    public List<TmdbGenre> getGenres(){
        TmdbGenreList response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/genre/movie/list")
                        .queryParam("language", "it-IT")
                        .build())
                .retrieve()
                .body(TmdbGenreList.class);

        return response != null ? response.genres() : List.of();
    }

    public List<TmdbProvider> getWatchProviders(Long movieId) {
        TmdbWatchProviders response = restClient.get()
                .uri("/movie/{id}/watch/providers", movieId)
                .retrieve()
                .body(TmdbWatchProviders.class);

        if (response == null || response.results() == null) {
            return List.of();
        }
        TmdbCountryProviders italy = response.results().get("IT");
        if (italy == null || italy.flatrate() == null) {
            return List.of();
        }
        return italy.flatrate();
    }
}
