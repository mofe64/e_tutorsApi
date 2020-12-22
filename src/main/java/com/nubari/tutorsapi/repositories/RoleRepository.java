package com.nubari.tutorsapi.repositories;

import com.nubari.tutorsapi.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findRoleByName(String name);
}
