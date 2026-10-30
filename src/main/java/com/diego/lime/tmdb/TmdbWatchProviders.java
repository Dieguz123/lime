package com.diego.lime.tmdb;

import java.util.Map;

public record TmdbWatchProviders(Map<String, TmdbCountryProviders> results) {
}
