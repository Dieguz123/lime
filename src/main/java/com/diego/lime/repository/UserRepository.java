package com.diego.lime.repository;

import com.diego.lime.model.Film;
import com.diego.lime.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
