package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.exceptions.CourseLimitReachedException;
import com.nubari.tutorsapi.exceptions.CourseNotFoundException;
import com.nubari.tutorsapi.exceptions.StudentNotFoundException;
import com.nubari.tutorsapi.exceptions.TutorNotFoundException;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.User;
import com.nubari.tutorsapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

//    @Autowired
//    ClassService classService;

    @Autowired
    CourseService courseService;

    @Override
    public void registerStudentForCourse(String studentId, String courseId) throws CourseNotFoundException, CourseLimitReachedException, StudentNotFoundException {
        registerStudentForACourse(studentId, courseId);
    }

    private void registerStudentForACourse(String studentId, String courseId) throws StudentNotFoundException, CourseNotFoundException, CourseLimitReachedException {
        User student = findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);
        courseService.addStudentToCourse(student.getId(), course.getId());

    }

    @Override
    public void removeStudentFromCourse(String studentId, String courseId) throws CourseNotFoundException, StudentNotFoundException {
        removeAStudentFromACourse(studentId, courseId);
    }

    private void removeAStudentFromACourse(String studentId, String courseId) throws StudentNotFoundException, CourseNotFoundException {
        User student = findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);
        courseService.removeStudentFromCourse(student.getId(), course.getId());
    }

    @Override
    public void registerTutorForCourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException {
        registerATutorForACourse(tutorId, courseId);
    }

    private void registerATutorForACourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException {
        User tutor = findTutorById(tutorId);
        Course course = courseService.findCourseById(courseId);
        courseService.addTutorToCourse(tutor.getId(), course.getId());
    }

    @Override
    public void removeTutorFromCourse(String tutorId, String courseId) throws CourseNotFoundException, TutorNotFoundException {
        removeATutorFromACourse(tutorId, courseId);
    }

    private void removeATutorFromACourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException {
        courseService.removeTutorFromCourse(tutorId, courseId);

    }

    @Override
    public void scheduleAClassForACourse(String tutorId, String courseId) {

    }

    @Override
    public void cancelAClass(String tutorId, String courseId) {

    }

    @Override
    public User findStudentById(String studentId) throws StudentNotFoundException {
        return findAStudentById(studentId);
    }

    private User findAStudentById(String studentId) throws StudentNotFoundException {
        Optional<User> userOptional = userRepository.findById(studentId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new StudentNotFoundException();
        }
    }

    @Override
    public User findTutorById(String tutorId) throws TutorNotFoundException {
        return findATutorById(tutorId);
    }

    private User findATutorById(String tutorId) throws TutorNotFoundException {
        Optional<User> userOptional = userRepository.findById(tutorId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new TutorNotFoundException();
        }
    }

    @Override
    public void saveUser(User user) {
        saveAUser(user);
    }

    private void saveAUser(User user) {
        userRepository.save(user);
    }
}
