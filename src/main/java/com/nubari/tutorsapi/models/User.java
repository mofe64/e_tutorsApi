package com.nubari.tutorsapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
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
    @Email
    private String email;
    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;
    @NotBlank
    private String username;
    @DBRef
    private List<Class> classesAttending = new ArrayList<>();
    @DBRef
    private List<Class> ClassesTaught = new ArrayList<>();
    @DBRef
    private List<Course> coursesRegisteredFor = new ArrayList<>();
    @DBRef
    private List<Course> coursesTeaching = new ArrayList<>();
    @DBRef
    private Set<Role> roles = new HashSet<>();

}
