package com.nubari.tutorsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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

    @DBRef
    private List<Class> classesAttending;
    @DBRef
    private List<Class> ClassesTaught;
    @DBRef
    private List<Course> coursesRegisteredFor;
    @DBRef
    private List<Course> coursesTeaching;
    @DBRef
    private Set<Role> roles;
}
