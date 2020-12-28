package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.dtos.StudentDto;
import com.nubari.tutorsapi.dtos.TutorDto;
import com.nubari.tutorsapi.exceptions.CourseLimitReachedException;
import com.nubari.tutorsapi.exceptions.CourseNotFoundException;
import com.nubari.tutorsapi.exceptions.StudentNotFoundException;
import com.nubari.tutorsapi.exceptions.TutorNotFoundException;
import com.nubari.tutorsapi.models.User;

import java.util.List;

public interface UserService {

    void registerStudentForCourse(String studentId, String courseId) throws CourseNotFoundException, CourseLimitReachedException, StudentNotFoundException;

    void removeStudentFromCourse(String studentId, String courseId) throws CourseNotFoundException, StudentNotFoundException;

    void registerTutorForCourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException;

    void removeTutorFromCourse(String tutorId, String courseId) throws CourseNotFoundException, TutorNotFoundException;

    void scheduleAClassForACourse(String tutorId, String courseId);

    void cancelAClass(String tutorId, String courseId);

    User findStudentById(String studentId) throws StudentNotFoundException;

    User findTutorById(String tutorId) throws TutorNotFoundException;

    void saveUser(User user);

    List<StudentDto> getAllStudents();

    List<TutorDto> getAllTutors();

    StudentDto getStudentDetails(String studentId) throws StudentNotFoundException;

    TutorDto getTutorDetails(String tutorId) throws TutorNotFoundException;

    StudentDto updateStudentDetails(String studentId, StudentDto studentDto) throws StudentNotFoundException;

    TutorDto updateTutorDetails(String tutorId, TutorDto tutorDto) throws TutorNotFoundException;

    void deleteStudent(String studentId) throws CourseNotFoundException, StudentNotFoundException;

    void deleteTutor(String tutorId) throws CourseNotFoundException, TutorNotFoundException;

}
