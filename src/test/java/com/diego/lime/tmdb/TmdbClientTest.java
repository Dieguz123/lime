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

    private static final String DISCOVER_URL =
            "https://api.test/3/discover/movie?language=it-IT&watch_region=IT"
                    + "&with_watch_monetization_types=flatrate&sort_by=popularity.desc&page=1";

    private MockRestServiceServer server;
    private TmdbClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        client = new TmdbClient(builder, new TmdbProperties("https://api.test/3", "test-token"));
    }

    @Test
    void getPopularSubscriptionMovies_parsesResponse() {
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
                      "vote_average": 8.4,
                      "genre_ids": [878, 28]
                    }
                  ]
                }
                """;

        server.expect(requestTo(DISCOVER_URL))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<TmdbMovie> movies = client.getPopularSubscriptionMovies(1);

        server.verify();
        assertEquals(1, movies.size());

        TmdbMovie movie = movies.get(0);
        assertEquals(27205L, movie.id());
        assertEquals("Inception", movie.title());
        assertEquals("Dom Cobb è un ladro specializzato nei sogni.", movie.overview());
        assertEquals("/inception.jpg", movie.posterPath());
        assertEquals("2010-07-15", movie.releaseDate());
        assertEquals(List.of(878L,28L), movie.genreIds());
    }

    @Test
    void getPopularSubscriptionMovies_invalidToken_throws() {
        server.expect(requestTo(DISCOVER_URL))
                .andRespond(withUnauthorizedRequest());

        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> client.getPopularSubscriptionMovies(1));
    }

    @Test
    void getGenres_parsesResponse() {
        String json = """
            {
              "genres": [
                { "id": 28, "name": "Azione" },
                { "id": 878, "name": "Fantascienza" }
              ]
            }
            """;

        server.expect(requestTo("https://api.test/3/genre/movie/list?language=it-IT"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<TmdbGenre> genres = client.getGenres();

        server.verify();
        assertEquals(2, genres.size());
        assertEquals(28L, genres.get(0).id());
        assertEquals("Azione", genres.get(0).name());
        assertEquals(878L, genres.get(1).id());
        assertEquals("Fantascienza", genres.get(1).name());
    }
}