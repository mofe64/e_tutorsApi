package com.nubari.tutorsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    private String id;
    private String courseName;
    private int studentLimit;
    @DBRef
    private List<User> registeredStudents = new ArrayList<>();
    @DBRef
    private List<User> tutors = new ArrayList<>();
    private int numberOfStudentsEnrolled;
    @DBRef
    private List<Class> classes = new ArrayList<>();
}
