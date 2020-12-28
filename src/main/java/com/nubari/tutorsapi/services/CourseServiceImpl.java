package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.dtos.ClassDto;
import com.nubari.tutorsapi.dtos.CourseDto;
import com.nubari.tutorsapi.exceptions.ClassCouldNotBeFoundException;
import com.nubari.tutorsapi.exceptions.CourseLimitReachedException;
import com.nubari.tutorsapi.exceptions.CourseNotFoundException;
import com.nubari.tutorsapi.exceptions.StudentNotFoundException;
import com.nubari.tutorsapi.exceptions.TutorNotFoundException;
import com.nubari.tutorsapi.models.Class;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.User;
import com.nubari.tutorsapi.repositories.ClassRepository;
import com.nubari.tutorsapi.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserService userService;
    @Autowired
    ClassRepository classRepository;

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Course courseToSave = courseDto.unpackDto();
        Course savedCourse = createCourse(courseToSave);
        CourseDto returnValue = new CourseDto();
        return returnValue.packDto(savedCourse);
    }

    private Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void addStudentToCourse(String studentId, String courseId) throws CourseNotFoundException, CourseLimitReachedException, StudentNotFoundException {
        addAStudentToACourse(studentId, courseId);
    }

    private void addAStudentToACourse(String studentId, String courseId) throws CourseNotFoundException, CourseLimitReachedException, StudentNotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            User student = userService.findStudentById(studentId);
            Course course = courseOptional.get();
            if (course.getStudentLimit() != 0 && (course.getNumberOfStudentsEnrolled() < course.getStudentLimit())) {
                course.getRegisteredStudents().add(student);
                student.getCoursesRegisteredFor().add(course);
                courseRepository.save(course);
                userService.saveUser(student);
            } else {
                throw new CourseLimitReachedException();
            }
        } else {
            throw new CourseNotFoundException();
        }

    }

    @Override
    public void addTutorToCourse(String tutorId, String courseId) throws CourseNotFoundException, TutorNotFoundException {
        addATutorToACourse(tutorId, courseId);
    }

    private void addATutorToACourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            User tutor = userService.findTutorById(tutorId);
            Course course = courseOptional.get();
            course.getTutors().add(tutor);
            courseRepository.save(course);
            userService.saveUser(tutor);
        } else {
            throw new CourseNotFoundException();
        }
    }

    @Override
    public ClassDto createClassForCourse(String courseId) throws CourseNotFoundException {
        ClassDto classDto = new ClassDto();
        Class newClass = createAClassForACourse(courseId);
        classDto = classDto.packDto(newClass);
        return classDto;

    }

    private Class createAClassForACourse(String courseId) throws CourseNotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            Class newClass = new Class();
            newClass.setCourseTaught(course);
            course.getClasses().add(newClass);
            List<User> studentsTakingCourse = course.getRegisteredStudents();
            List<User> tutorsHandlingACourse = course.getTutors();
            for (User student : studentsTakingCourse) {
                newClass.getStudents().add(student);
            }
            for (User tutor : tutorsHandlingACourse) {
                newClass.getTutors().add(tutor);
            }
            courseRepository.save(course);
            classRepository.save(newClass);
            return newClass;
        } else {
            throw new CourseNotFoundException();
        }
    }

    @Override
    public void cancelClassForCourse(String classId) throws ClassCouldNotBeFoundException {
        cancelAClassForACourse(classId);
    }

    private void cancelAClassForACourse(String classId) throws ClassCouldNotBeFoundException {
        Optional<Class> classToDelete = classRepository.findById(classId);
        if (classToDelete.isPresent()) {
            classRepository.delete(classToDelete.get());
        } else {
            throw new ClassCouldNotBeFoundException();
        }
    }

    @Override
    public void deleteCourse(String courseId) throws StudentNotFoundException, TutorNotFoundException, CourseNotFoundException {
        deleteACourse(courseId);
    }

    private void deleteACourse(String courseId) throws StudentNotFoundException, TutorNotFoundException, CourseNotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course courseToDelete = courseOptional.get();
            List<Class> existingClassesForCourseToDelete = courseToDelete.getClasses();
            List<User> studentsAttendingCourseToDelete = courseToDelete.getRegisteredStudents();
            List<User> tutorsTakingCourseToDelete = courseToDelete.getTutors();
            if (existingClassesForCourseToDelete.size() > 0) {
                for (Class existingClass : existingClassesForCourseToDelete) {
                    classRepository.delete(existingClass);
                }
            }
            if (studentsAttendingCourseToDelete.size() > 0) {
                for (User student : studentsAttendingCourseToDelete) {
                    removeAStudentFromACourse(student.getId(), courseId);
                }
            }

            if (tutorsTakingCourseToDelete.size() > 0) {
                for (User tutor : tutorsTakingCourseToDelete) {
                    removeATutorFromACourse(tutor.getId(), courseId);
                }
            }
            courseRepository.delete(courseToDelete);
        }
    }

    @Override
    public List<CourseDto> getAllCourses() {
        List<Course> courses = getAllExistingCourses();
        List<CourseDto> courseDtos = new ArrayList<>();
        CourseDto courseDto = new CourseDto();
        for (Course course : courses) {
            courseDtos.add(courseDto.packDto(course));
        }
        return courseDtos;
    }

    private List<Course> getAllExistingCourses() {
        return courseRepository.findAll();
    }

    @Override
    public CourseDto getCourseDetails(String courseId) throws CourseNotFoundException {
        Course foundCourse = getACourseDetail(courseId);
        return new CourseDto().packDto(foundCourse);

    }

    private Course getACourseDetail(String courseId) throws CourseNotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            return courseOptional.get();
        } else {
            throw new CourseNotFoundException();
        }
    }

    @Override
    public CourseDto updateCourseDetails(String courseId, CourseDto courseDto) throws CourseNotFoundException {
        Course updatedCourse = courseDto.unpackDto();
        Course course = updateCourseDetails(courseId, updatedCourse);
        return new CourseDto().packDto(course);
    }

    private Course updateCourseDetails(String courseId, Course updatedCourse) throws CourseNotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            if (!updatedCourse.getCourseName().equals("") && updatedCourse.getCourseName() != null) {
                course.setCourseName(updatedCourse.getCourseName());
            }
            if (updatedCourse.getStudentLimit() != 0) {
                course.setStudentLimit(updatedCourse.getStudentLimit());
            }
            return courseRepository.save(course);
        } else {
            throw new CourseNotFoundException();
        }
    }


    @Override
    public void removeStudentFromCourse(String studentId, String courseId) throws StudentNotFoundException, CourseNotFoundException {
        removeAStudentFromACourse(studentId, courseId);
    }

    private void removeAStudentFromACourse(String studentId, String courseId) throws StudentNotFoundException, CourseNotFoundException {
        User student = userService.findStudentById(studentId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.getRegisteredStudents().remove(student);
            student.getCoursesRegisteredFor().remove(course);
            courseRepository.save(course);
            userService.saveUser(student);
        } else {
            throw new CourseNotFoundException();
        }
    }

    @Override
    public void removeTutorFromCourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException {
        removeATutorFromACourse(tutorId, courseId);
    }

    private void removeATutorFromACourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException {
        User tutor = userService.findTutorById(tutorId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.getTutors().remove(tutor);
            tutor.getCoursesTeaching().remove(course);
            courseRepository.save(course);
            userService.saveUser(tutor);
        } else {
            throw new CourseNotFoundException();
        }
    }

    @Override
    public Course findCourseById(String courseId) throws CourseNotFoundException {
        return findACourseById(courseId);
    }

    private Course findACourseById(String courseId) throws CourseNotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            return courseOptional.get();
        } else {
            throw new CourseNotFoundException();
        }
    }
}
