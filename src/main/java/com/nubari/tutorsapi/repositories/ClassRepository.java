package com.nubari.tutorsapi.repositories;

import com.nubari.tutorsapi.models.Class;
import com.nubari.tutorsapi.models.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClassRepository extends MongoRepository<Class, String> {
}
