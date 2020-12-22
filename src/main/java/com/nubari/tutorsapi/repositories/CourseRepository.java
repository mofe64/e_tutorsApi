package com.nubari.tutorsapi.repositories;

import com.nubari.tutorsapi.models.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {
}
