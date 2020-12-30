package com.nubari.tutorsapi.controllers;

import com.nubari.tutorsapi.dtos.*;
import com.nubari.tutorsapi.exceptions.EmailAlreadyExistsException;
import com.nubari.tutorsapi.exceptions.UserRoleNotFoundException;
import com.nubari.tutorsapi.exceptions.UsernameAlreadyExistsException;
import com.nubari.tutorsapi.models.Role;
import com.nubari.tutorsapi.models.User;
import com.nubari.tutorsapi.security.jwt.TokenProvider;
import com.nubari.tutorsapi.services.RoleService;
import com.nubari.tutorsapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user/")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @PostMapping("login")
    public ResponseEntity<?> generateToken(@RequestBody UserLoginDto userLoginDto) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(),
                        userLoginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(authentication);
        return new ResponseEntity<>(new AuthToken(token), HttpStatus.OK);
    }

    @PostMapping("register/student")
    public ResponseEntity<?> registerStudent(@RequestBody UserDto userDto) {
        try {
            UserDto newStudent = userService.registerStudent(userDto);
            return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
        } catch (UserRoleNotFoundException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        } catch (UsernameAlreadyExistsException usernameAlreadyExistsException) {
            return new ResponseEntity<>(new APIResponseModel(false, "Username already exists"),
                    HttpStatus.BAD_REQUEST);
        } catch (EmailAlreadyExistsException emailAlreadyExistsException) {
            return new ResponseEntity<>(new APIResponseModel(false, "Email already exists"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("register/tutor")
    public ResponseEntity<?> registerTutor(@RequestBody UserDto userDto) {
        try {
            UserDto newTutor = userService.registerTutor(userDto);
            return new ResponseEntity<>(newTutor, HttpStatus.CREATED);
        } catch (UserRoleNotFoundException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        } catch (UsernameAlreadyExistsException usernameAlreadyExistsException) {
            return new ResponseEntity<>(new APIResponseModel(false, "Username already exists"),
                    HttpStatus.BAD_REQUEST);
        } catch (EmailAlreadyExistsException emailAlreadyExistsException) {
            return new ResponseEntity<>(new APIResponseModel(false, "Email already exists"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("roles/new")
    public ResponseEntity<?> createNewRole(@RequestBody Role role) {
        roleService.createNewRole(role);
        return new ResponseEntity<>(new APIResponseModel(true, "Role created " + role.getName()), HttpStatus.CREATED);
    }
}
