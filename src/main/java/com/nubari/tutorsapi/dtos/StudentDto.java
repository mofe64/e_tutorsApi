package com.nubari.tutorsapi.dtos;

import com.nubari.tutorsapi.models.Class;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private List<Class> classesAttending = new ArrayList<>();
    private List<Course> coursesRegisteredFor = new ArrayList<>();


    public User unpackDto() {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setUsername(username);
        user.setUserType("student");
        user.setPassword(password);
        return user;
    }

    public StudentDto packDto(User user) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(user.getId());
        studentDto.setFirstname(user.getFirstname());
        studentDto.setLastname(user.getLastname());
        studentDto.setEmail(user.getEmail());
        studentDto.setClassesAttending(user.getClassesAttending());
        studentDto.setCoursesRegisteredFor(user.getCoursesRegisteredFor());
        return studentDto;
    }
}
