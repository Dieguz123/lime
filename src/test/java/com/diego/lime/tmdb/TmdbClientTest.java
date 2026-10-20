package com.diego.lime.tmdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

class TmdbClientTest {

    private static final String POPULAR_URL =
            "https://api.test/3/movie/popular?language=it-IT&page=1";

    private MockRestServiceServer server;
    private TmdbClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        client = new TmdbClient(builder, new TmdbProperties("https://api.test/3", "test-token"));
    }

    @Test
    void getPopularMovies_parsesResponse() {
        String json = """
                {
                  "page": 1,
                  "total_pages": 500,
                  "results": [
                    {
                      "id": 27205,
                      "title": "Inception",
                      "overview": "Dom Cobb è un ladro specializzato nei sogni.",
                      "poster_path": "/inception.jpg",
                      "release_date": "2010-07-15",
                      "vote_average": 8.4
                    }
                  ]
                }
                """;

        server.expect(requestTo(POPULAR_URL))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<TmdbMovie> movies = client.getPopularMovies(1);

        server.verify();
        assertEquals(1, movies.size());

        TmdbMovie movie = movies.get(0);
        assertEquals(27205L, movie.id());
        assertEquals("Inception", movie.title());
        assertEquals("Dom Cobb è un ladro specializzato nei sogni.", movie.overview());
        assertEquals("/inception.jpg", movie.posterPath());
        assertEquals("2010-07-15", movie.releaseDate());
    }

    @Test
    void getPopularMovies_invalidToken_throws() {
        server.expect(requestTo(POPULAR_URL))
                .andRespond(withUnauthorizedRequest());

        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> client.getPopularMovies(1));
    }
}