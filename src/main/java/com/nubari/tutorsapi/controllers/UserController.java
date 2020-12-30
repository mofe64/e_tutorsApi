package com.nubari.tutorsapi.controllers;

import com.nubari.tutorsapi.dtos.APIResponseModel;
import com.nubari.tutorsapi.dtos.UserDto;
import com.nubari.tutorsapi.exceptions.*;
import com.nubari.tutorsapi.models.User;
import com.nubari.tutorsapi.services.ClassService;
import com.nubari.tutorsapi.services.CourseService;
import com.nubari.tutorsapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user/")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    CourseService courseService;
    @Autowired
    ClassService classService;


    @PreAuthorize("hasRole('Student')")
    @GetMapping("students/all")
    public ResponseEntity<?> getStudents() {
        try {
            List<UserDto> students = userService.getAllStudents();
            return new ResponseEntity<>(students, HttpStatus.OK);
        } catch (UserRoleNotFoundException userRoleNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, userRoleNotFoundException.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('Student')")
    @GetMapping("students/{studentId}")
    public ResponseEntity<?> getAStudent(@PathVariable String studentId) {
        try {
            UserDto userDto = userService.getStudentDetails(studentId);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (StudentNotFoundException studentNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No student found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('Student')")
    @GetMapping("tutors/all")
    public ResponseEntity<?> getTutors() {
        try {
            List<UserDto> tutors = userService.getAllTutors();
            return new ResponseEntity<>(tutors, HttpStatus.OK);
        } catch (UserRoleNotFoundException userRoleNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, userRoleNotFoundException.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('Student')")
    @GetMapping("tutors/{tutorId}")
    public ResponseEntity<?> getATutor(@PathVariable String tutorId) {
        try {
            UserDto userDto = userService.getTutorDetails(tutorId);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (TutorNotFoundException tutorNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No tutor found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('Student')")
    @PatchMapping("student/{studentId}")
    public ResponseEntity<?> updateStudentDetails(@RequestBody UserDto updatedStudent, @PathVariable String studentId) {
        try {
            UserDto userDto = userService.updateStudentDetails(studentId, updatedStudent);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (StudentNotFoundException studentNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Student found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('Student')")
    @PatchMapping("tutor/{tutorId}")
    public ResponseEntity<?> updateTutorDetails(@RequestBody UserDto updatedTutor, @PathVariable String tutorId) {
        try {
            UserDto userDto = userService.updateTutorDetails(tutorId, updatedTutor);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (TutorNotFoundException tutorNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Student found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('Student')")
    @GetMapping("student/{studentId}/course/{courseId}/enroll")
    public ResponseEntity<?> registerStudentForCourse(@PathVariable String courseId, @PathVariable String studentId) {
        try {
            userService.registerStudentForCourse(studentId, courseId);
            return new ResponseEntity<>(new APIResponseModel(true, "Student successfully added to course"),
                    HttpStatus.OK);
        } catch (StudentNotFoundException studentNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No student found with that Id"),
                    HttpStatus.BAD_REQUEST);
        } catch (CourseNotFoundException courseNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Course found with that Id"),
                    HttpStatus.BAD_REQUEST);
        } catch (CourseLimitReachedException courseLimitReachedException) {
            return new ResponseEntity<>(new APIResponseModel(false, "Course limit reached"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('Student')")
    @GetMapping("studnet/{studentId}/course/{courseId}/unenroll")
    public ResponseEntity<?> removeStudentFromCourse(@PathVariable String courseId, @PathVariable String studentId) {
        try {
            userService.removeStudentFromCourse(studentId, courseId);
            return new ResponseEntity<>(new APIResponseModel(true, "Student removed successfully"),
                    HttpStatus.OK);
        } catch (StudentNotFoundException studentNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No course found with that Id"),
                    HttpStatus.BAD_REQUEST);
        } catch (CourseNotFoundException courseNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No couse found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('Student')")
    @GetMapping("tutor/{tutorId}/course/{courseId}/enroll")
    public ResponseEntity<?> registerTutorForCourse(@PathVariable String courseId, @PathVariable String tutorId) {
        try {
            userService.registerTutorForCourse(tutorId, courseId);
            return new ResponseEntity<>(new APIResponseModel(true, "Tutor successfully added to course"),
                    HttpStatus.OK);
        } catch (TutorNotFoundException tutorNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Tutor found with that Id"),
                    HttpStatus.BAD_REQUEST);
        } catch (CourseNotFoundException courseNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Course found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('Student')")
    @GetMapping("tutor/{tutorId}/course/{courseId}/unenroll")
    public ResponseEntity<?> removeTutorFromCourse(@PathVariable String courseId, @PathVariable String tutorId) {
        try {
            userService.removeTutorFromCourse(tutorId, courseId);
            return new ResponseEntity<>(new APIResponseModel(true, "Tutor removed from course"),
                    HttpStatus.OK);
        } catch (TutorNotFoundException tutorNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Tutor found with that Id"),
                    HttpStatus.BAD_REQUEST);
        } catch (CourseNotFoundException courseNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Course found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }
    }




}
