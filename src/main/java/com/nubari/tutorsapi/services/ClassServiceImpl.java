package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.dtos.ClassDto;
import com.nubari.tutorsapi.exceptions.ClassCouldNotBeFoundException;
import com.nubari.tutorsapi.exceptions.CourseNotFoundException;
import com.nubari.tutorsapi.exceptions.TutorNotFoundException;
import com.nubari.tutorsapi.models.Class;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.User;
import com.nubari.tutorsapi.repositories.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    UserService userService;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    CourseService courseService;

    @Override
    public List<ClassDto> getAllClasses() {
        List<ClassDto> classDtos = new ArrayList<>();
        List<Class> classes = getAllExistingClasses();
        for (Class foundClass : classes) {
            ClassDto classDto = new ClassDto();
            classDtos.add(classDto.packDto(foundClass));
        }
        return classDtos;
    }

    private List<Class> getAllExistingClasses() {
        return classRepository.findAll();
    }

    @Override
    public ClassDto getAClass(String classId) throws ClassCouldNotBeFoundException {
        ClassDto classDto = new ClassDto();
        return classDto.packDto(getClass(classId));
    }

    private Class getClass(String classId) throws ClassCouldNotBeFoundException {
        Optional<Class> classOptional = classRepository.findById(classId);
        if (classOptional.isPresent()) {
            return classOptional.get();
        } else {
            throw new ClassCouldNotBeFoundException();
        }
    }

    @Override
    public Class saveClass(Class newClass) {
        return saveANewClass(newClass);
    }

    private Class saveANewClass(Class newClass) {
        return classRepository.save(newClass);
    }

    @Override
    public void createClass(String tutorId, Course course) throws TutorNotFoundException {
        Class newClass = new Class();
        newClass.setCourseTaught(course);
        User classTutor = userService.findTutorById(tutorId);
        newClass.getTutors().add(classTutor);
        saveANewClass(newClass);
    }


    @Override
    public void cancelClass(String classId) throws ClassCouldNotBeFoundException {
        cancelAClass(classId);
    }

    private void cancelAClass(String classId) throws ClassCouldNotBeFoundException {
        Optional<Class> classOptional = classRepository.findById(classId);
        if (classOptional.isPresent()) {
            Class classToCancel = classOptional.get();
            List<User> classTutors = classToCancel.getTutors();
            List<User> classStudents = classToCancel.getStudents();
            for (User student : classStudents) {
                student.getClassesAttending().remove(classToCancel);
            }
            for (User tutor : classTutors) {
                tutor.getClassesTaught().remove(classToCancel);
            }
            classRepository.delete(classOptional.get());
        } else {
            throw new ClassCouldNotBeFoundException();
        }
    }

    @Override
    public List<ClassDto> getClassesForCourse(String courseId) throws CourseNotFoundException {
        List<ClassDto> classDtos = new ArrayList<>();
        List<Class> foundClasses = getClassesForACourse(courseId);
        for (Class foundClass : foundClasses) {
            ClassDto classDto = new ClassDto();
            classDtos.add(classDto.packDto(foundClass));
        }
        return classDtos;
    }

    private List<Class> getClassesForACourse(String courseId) throws CourseNotFoundException {
        Course course = courseService.findCourseById(courseId);
        List<Class> allClasses = classRepository.findAll();
        List<Class> classes = new ArrayList<>();
        for (Class foundClass : allClasses) {
            if (foundClass.getCourseTaught().equals(course)) {
                classes.add(foundClass);
            }
        }
        return classes;
    }
}
