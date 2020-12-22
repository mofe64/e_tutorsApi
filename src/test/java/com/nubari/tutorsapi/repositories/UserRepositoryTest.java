package com.nubari.tutorsapi.repositories;

import com.nubari.tutorsapi.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testUserCanBeCreatedAndSaved() {
        User user = new User();
        user.setFirstname("Mofe");
        user.setLastname("Jamal");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertEquals(user.getFirstname(), savedUser.getFirstname());
        assertEquals(user.getLastname(), savedUser.getLastname());
    }
}