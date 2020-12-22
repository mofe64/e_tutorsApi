package com.nubari.tutorsapi.repositories;

import com.nubari.tutorsapi.models.Class;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassRepository extends MongoRepository<Class, String> {

}
