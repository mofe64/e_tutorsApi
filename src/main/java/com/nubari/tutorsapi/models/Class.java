package com.nubari.tutorsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;


@Document(collection = "classes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Class {
    @Id
    private String id;
    private LocalDate classDate;
    @DBRef
    private Course courseTaught;
    @DBRef
    private User tutor;
    @DBRef
    private List<User> students;

}
