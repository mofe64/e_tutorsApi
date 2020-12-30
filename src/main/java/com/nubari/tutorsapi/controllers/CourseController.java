package com.nubari.tutorsapi.controllers;

import com.nubari.tutorsapi.dtos.APIResponseModel;
import com.nubari.tutorsapi.dtos.ClassDto;
import com.nubari.tutorsapi.dtos.CourseDto;
import com.nubari.tutorsapi.exceptions.ClassCouldNotBeFoundException;
import com.nubari.tutorsapi.exceptions.CourseNotFoundException;
import com.nubari.tutorsapi.exceptions.StudentNotFoundException;
import com.nubari.tutorsapi.exceptions.TutorNotFoundException;
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
@RequestMapping("/api/v1/courses/")
public class CourseController {
    @Autowired
    UserService userService;
    @Autowired
    CourseService courseService;
    @Autowired
    ClassService classService;

    @PreAuthorize("hasRole('Student')")
    @PostMapping("new")
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto) {
        CourseDto response = courseService.createCourse(courseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('Student')")
    @GetMapping("all")
    public ResponseEntity<?> getAllCourses() {
        List<CourseDto> courseList = courseService.getAllCourses();
        return new ResponseEntity<>(courseList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Student')")
    @GetMapping("{courseId}")
    public ResponseEntity<?> getCourse(@PathVariable String courseId) {
        try {
            CourseDto response = courseService.getCourseDetails(courseId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CourseNotFoundException courseNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Course found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("{courseId}")
    public ResponseEntity<?> updateCourseDetails(@RequestBody CourseDto courseDto, @PathVariable String courseId) {
        try {
            CourseDto response = courseService.updateCourseDetails(courseId, courseDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CourseNotFoundException courseNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Course Found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {
        try {
            courseService.deleteCourse(courseId);
            return new ResponseEntity<>(new APIResponseModel(true, "Course deleted Successfully"),
                    HttpStatus.NO_CONTENT);
        } catch (CourseNotFoundException courseNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Course found with that Id"),
                    HttpStatus.BAD_REQUEST);
        } catch (StudentNotFoundException studentNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Student found with that Id"),
                    HttpStatus.BAD_REQUEST);
        } catch (TutorNotFoundException tutorNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Tutor found with that Id"),
                    HttpStatus.BAD_REQUEST);
        } catch (ClassCouldNotBeFoundException classCouldNotBeFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Class found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('Student')")
    @PostMapping("{courseId}/class/create")
    public ResponseEntity<?> createNewClass(@RequestBody ClassDto classDto, @PathVariable String courseId) {
        try {
            ClassDto classDto1 = courseService.createClassForCourse(courseId);
            return new ResponseEntity<>(classDto1, HttpStatus.CREATED);
        } catch (CourseNotFoundException courseNotFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No course found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('Student')")
    @PostMapping("{classId}/class/cancel")
    public ResponseEntity<?> cancelClass(@PathVariable String classId) {
        try {
            courseService.cancelClassForCourse(classId);
            return new ResponseEntity<>(new APIResponseModel(false, "Class canceled"), HttpStatus.CREATED);
        } catch (ClassCouldNotBeFoundException classCouldNotBeFoundException) {
            return new ResponseEntity<>(new APIResponseModel(false, "No Class found with that Id"),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
