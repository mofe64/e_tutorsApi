package com.nubari.tutorsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String username;
    private String userType;
    @DBRef
    private List<Class> classesAttending = new ArrayList<>();
    @DBRef
    private List<Class> ClassesTaught = new ArrayList<>();
    @DBRef
    private List<Course> coursesRegisteredFor = new ArrayList<>();
    @DBRef
    private List<Course> coursesTeaching = new ArrayList<>();
    @DBRef
    private Set<Role> roles;

}
