package com.nubari.tutorsapi.services;


import com.nubari.tutorsapi.dtos.UserDto;
import com.nubari.tutorsapi.exceptions.*;
import com.nubari.tutorsapi.models.User;

import java.util.List;

public interface UserService {


    void registerStudentForCourse(String studentId, String courseId) throws CourseNotFoundException, CourseLimitReachedException, StudentNotFoundException;

    void removeStudentFromCourse(String studentId, String courseId) throws CourseNotFoundException, StudentNotFoundException;

    void registerTutorForCourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException;

    void removeTutorFromCourse(String tutorId, String courseId) throws CourseNotFoundException, TutorNotFoundException;

    User findStudentById(String studentId) throws StudentNotFoundException;

    User findTutorById(String tutorId) throws TutorNotFoundException;

    UserDto registerStudent(UserDto userDto) throws UserRoleNotFoundException, UsernameAlreadyExistsException, EmailAlreadyExistsException;

    UserDto registerTutor(UserDto userDto) throws UserRoleNotFoundException, UsernameAlreadyExistsException, EmailAlreadyExistsException;

    void saveUser(User user);

    List<UserDto> getAllStudents() throws UserRoleNotFoundException;

    List<UserDto> getAllTutors() throws UserRoleNotFoundException;

    UserDto getStudentDetails(String studentId) throws StudentNotFoundException;

    UserDto getTutorDetails(String tutorId) throws TutorNotFoundException;

    UserDto updateStudentDetails(String studentId, UserDto userDto) throws StudentNotFoundException;

    UserDto updateTutorDetails(String tutorId, UserDto userDto) throws TutorNotFoundException;

    void deleteStudent(String studentId) throws CourseNotFoundException, StudentNotFoundException;

    void deleteTutor(String tutorId) throws CourseNotFoundException, TutorNotFoundException;

    boolean checkIfUserWithThisUsernameExists(String username);

    boolean checkIfUserWithThisEmailExists(String email);

}
