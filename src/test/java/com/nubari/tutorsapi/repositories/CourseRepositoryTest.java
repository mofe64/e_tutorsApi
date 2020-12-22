package com.nubari.tutorsapi.repositories;

import com.nubari.tutorsapi.models.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;
@DataMongoTest
class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;
    @BeforeEach
    void setUp() {
    }

    @AfterEachgi
    void tearDown() {
    }
    @Test
    void testCourseCanBeCreatedAndSaved(){
        Course course = new Course();
        course.setCourseName("Java How to program");
        course.setStudentLimit(100);
        Course savedCourse = courseRepository.save(course);
        assertNotNull(savedCourse.getId());
        assertEquals(course.getCourseName(),savedCourse.getCourseName());
        assertEquals(course.getStudentLimit(),savedCourse.getStudentLimit());
    }
}