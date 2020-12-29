package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.exceptions.UserRoleNotFoundException;
import com.nubari.tutorsapi.models.Role;
import com.nubari.tutorsapi.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(String name) throws UserRoleNotFoundException {
        return findRoleByName(name);
    }

    private Role findRoleByName(String name) throws UserRoleNotFoundException {
        Optional<Role> roleOptional = roleRepository.findRoleByName(name);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        } else {
            throw new UserRoleNotFoundException();
        }
    }

    @Override
    public void createNewRole(Role role) {
        createANewRole(role);
    }

    private void createANewRole(Role role) {
        roleRepository.save(role);
    }
}

