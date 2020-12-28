package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.repositories.ClassRepository;
import com.nubari.tutorsapi.repositories.CourseRepository;
import com.nubari.tutorsapi.repositories.RoleRepository;
import com.nubari.tutorsapi.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ClassRepository classRepository;
    @Mock
    CourseRepository courseRepository;
    @Mock
    RoleRepository roleRepository;
    @InjectMocks
    UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }
}