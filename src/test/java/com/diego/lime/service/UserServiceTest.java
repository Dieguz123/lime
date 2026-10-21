package com.diego.lime.service;

import com.diego.lime.exception.InvalidCredentialsException;
import com.diego.lime.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void register_SavesHashPassword() {
        String rawPassword = "12345";
        User diego = userService.register("Diego", rawPassword);

        boolean match = passwordEncoder.matches(rawPassword, diego.getPasswordHash());

        assertNotEquals(rawPassword, diego.getPasswordHash());
        assertTrue(match);
    }

    @Test
    void register_normalizesUsername() {
        User diego = userService.register("Diego    ", "12345");

        assertEquals("diego", diego.getUsername());
    }

    @Test
    void register_duplicateUsername_throws() {
        User diego = userService.register("Diego", "12345");

        assertThrows(IllegalStateException.class,
                () -> userService.register("DIEGO", "12345"));
    }

    @Test
    void authenticate_validCredentials_returnsUser() {
        User diego = userService.register("Diego", "12345");
        User authenticated = userService.authenticate("DIEGO", "12345");

        assertEquals(diego.getId(), authenticated.getId());
    }

    @Test
    void authenticate_wrongPassword_throws() {

        assertThrows(InvalidCredentialsException.class,
                () -> userService.authenticate("DIEGO", "abcde"));
    }
}
