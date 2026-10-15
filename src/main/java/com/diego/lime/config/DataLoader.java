package com.diego.lime.config;

import com.diego.lime.model.Film;
import com.diego.lime.repository.FilmRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final FilmRepository repository;

    public DataLoader(FilmRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (repository.count() > 0) {
            return;
        }

        repository.save(new Film("Inception", 2010, "Prime", "Fantascienza"));
        repository.save(new Film("Star Trek", 2009, "Prime", "Fantascienza"));
        repository.save(new Film("Interstellar", 2014, "Prime", "Fantascienza"));
    }
}
