package com.nubari.tutorsapi.repositories;

import com.nubari.tutorsapi.models.Class;
import com.nubari.tutorsapi.models.Role;
import com.nubari.tutorsapi.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class ClassRepositoryTest {
    @Autowired
    ClassRepository classRepository;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testNewClassCanBeCreatedAndSaved() {
        User tutor = new User();
        tutor.setFirstname("Mofe");
        tutor.setId("001");
        User student = new User();
        student.setFirstname("Tayo");
        student.setId("002");
        Class newClass = new Class();
        newClass.setClassDate(LocalDate.now());
        newClass.getTutors().add(tutor);
        newClass.getStudents().add(student);
        Class savedClass = classRepository.save(newClass);
        assertNotNull(savedClass.getId());
        assertEquals("Mofe", savedClass.getTutors().get(0).getFirstname());
        assertEquals(1, savedClass.getStudents().size());

    }
}