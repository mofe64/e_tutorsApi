package com.nubari.tutorsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "classes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Class {
    @Id
    private String id;
    @DBRef
    private Course courseTaught;
    @DBRef
    private List<User> tutors = new ArrayList<>();
    @DBRef
    private List<User> students = new ArrayList<>();

}
