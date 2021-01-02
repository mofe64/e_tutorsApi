package com.nubari.tutorsapi.repositories;

import com.nubari.tutorsapi.models.Class;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findUsersByCoursesRegisteredFor(Course course);

    List<User> findUsersByCoursesTeaching(Course course);

    List<User> findUsersByClassesAttending(Class studentClass);

    List<User> findUsersByClassesTaught(Class tutorClass);
}
