package com.nubari.tutorsapi.dtos;

import com.nubari.tutorsapi.models.Class;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDto {
    private String id;
    @NonNull
    private Course courseTaught;
    private List<User> tutors = new ArrayList<>();
    private List<User> students = new ArrayList<>();

    public Class unpackDto() {
        Class newClass = new Class();
        newClass.setCourseTaught(courseTaught);
        newClass.setStudents(students);
        newClass.setTutors(tutors);
        return newClass;
    }

    public ClassDto packDto(Class classObj) {
        ClassDto classDto = new ClassDto();
        classDto.setCourseTaught(classObj.getCourseTaught());
        classDto.setStudents(classObj.getStudents());
        classDto.setTutors(classObj.getTutors());
        classDto.setId(classObj.getId());
        return classDto;
    }
}
