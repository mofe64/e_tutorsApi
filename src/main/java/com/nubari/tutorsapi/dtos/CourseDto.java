package com.nubari.tutorsapi.dtos;

import com.nubari.tutorsapi.models.Class;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private String id;
    @NotBlank
    @Min(value = 3,message = "Course name must be at least 3 characters long")
    private String courseName;
    private int studentLimit;
    private List<User> registeredStudents = new ArrayList<>();
    private List<User> tutors = new ArrayList<>();
    private int numberOfStudentsEnrolled;
    private List<Class> classes = new ArrayList<>();

    public Course unpackDto() {
        Course course = new Course();
        course.setCourseName(courseName);
        course.setStudentLimit(studentLimit);
        course.setRegisteredStudents(registeredStudents);
        course.setTutors(tutors);
        course.setNumberOfStudentsEnrolled(numberOfStudentsEnrolled);
        course.setClasses(classes);
        return course;
    }

    public CourseDto packDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setCourseName(course.getCourseName());
        courseDto.setRegisteredStudents(course.getRegisteredStudents());
        courseDto.setTutors(course.getTutors());
        courseDto.setStudentLimit(course.getStudentLimit());
        courseDto.setNumberOfStudentsEnrolled(course.getNumberOfStudentsEnrolled());
        courseDto.setClasses(course.getClasses());
        return courseDto;
    }
}
