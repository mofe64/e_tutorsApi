package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.dtos.ClassDto;
import com.nubari.tutorsapi.dtos.CourseDto;
import com.nubari.tutorsapi.exceptions.*;
import com.nubari.tutorsapi.models.Course;

import java.util.List;

public interface CourseService {
    CourseDto createCourse(CourseDto courseDto);

    void addStudentToCourse(String studentId, String courseId) throws CourseNotFoundException, StudentNotFoundException, CourseLimitReachedException;

    void addTutorToCourse(String tutorId, String courseId) throws CourseNotFoundException, TutorNotFoundException;

    ClassDto createClassForCourse(String courseId) throws CourseNotFoundException;

    void cancelClassForCourse(String classId) throws ClassCouldNotBeFoundException;

    void deleteCourse(String courseId) throws StudentNotFoundException, TutorNotFoundException, CourseNotFoundException, ClassCouldNotBeFoundException;

    List<CourseDto> getAllCourses();

    CourseDto getCourseDetails(String courseId) throws CourseNotFoundException;

    CourseDto updateCourseDetails(String courseId, CourseDto courseDto) throws CourseNotFoundException;

    void removeStudentFromCourse(String studentId, String courseId) throws StudentNotFoundException, CourseNotFoundException;

    void removeTutorFromCourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException;

    Course findCourseById(String courseId) throws CourseNotFoundException;

}
