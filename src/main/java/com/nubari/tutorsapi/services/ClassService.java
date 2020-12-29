package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.dtos.ClassDto;
import com.nubari.tutorsapi.exceptions.ClassCouldNotBeFoundException;
import com.nubari.tutorsapi.exceptions.CourseNotFoundException;
import com.nubari.tutorsapi.models.Class;

import java.util.List;

public interface ClassService {
    List<ClassDto> getAllClasses();

    ClassDto getAClass(String classId) throws ClassCouldNotBeFoundException;

    void cancelClass(String classId) throws ClassCouldNotBeFoundException;

    List<ClassDto> getClassesForCourse(String courseId) throws CourseNotFoundException;

    Class saveClass(Class newClass) ;

}
