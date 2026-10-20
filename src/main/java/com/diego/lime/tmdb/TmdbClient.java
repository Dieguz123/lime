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

    public List<TmdbMovie> getPopularMovies( int page ){
        TmdbPage response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/popular")
                        .queryParam("language", "it-IT")
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .body(TmdbPage.class);

        return response != null ? response.results() : List.of();
    }
}
