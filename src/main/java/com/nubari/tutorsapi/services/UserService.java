package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.exceptions.CourseLimitReachedException;
import com.nubari.tutorsapi.exceptions.CourseNotFoundException;
import com.nubari.tutorsapi.exceptions.StudentNotFoundException;
import com.nubari.tutorsapi.exceptions.TutorNotFoundException;
import com.nubari.tutorsapi.models.User;

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
}
