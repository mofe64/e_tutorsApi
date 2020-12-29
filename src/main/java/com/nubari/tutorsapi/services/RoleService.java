package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.exceptions.UserRoleNotFoundException;
import com.nubari.tutorsapi.models.Role;

public interface RoleService {
    Role findByName(String name) throws UserRoleNotFoundException;
    void createNewRole(Role role);
}
