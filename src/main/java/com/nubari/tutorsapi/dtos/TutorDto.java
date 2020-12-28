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

@SuppressWarnings("ALL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorDto {
    private String id;
    @NotBlank
    @Min(value = 3, message = "firstname must be at least 3 characters long")
    private String firstname;
    @NotBlank
    @Min(value = 3, message = "lastname must be at least 3 characters long")
    private String lastname;
    @Email
    private String email;
    @NotBlank
    @Min(value = 3, message = "username must be at least 3 characters long")
    private String username;
    @NotBlank
    @Min(value = 7, message = "Password must be at least 7 characters")
    private String password;
    private List<Class> ClassesTaught = new ArrayList<>();
    private List<Course> coursesTeaching = new ArrayList<>();

    public User unpackDto() {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setUsername(username);
        user.setUserType("tutor");
        user.setPassword(password);
        return user;
    }

    public TutorDto packDto(User user) {
        TutorDto tutorDto = new TutorDto();
        tutorDto.setId(user.getId());
        tutorDto.setFirstname(user.getFirstname());
        tutorDto.setLastname(user.getLastname());
        tutorDto.setEmail(user.getEmail());
        tutorDto.setClassesTaught(user.getClassesAttending());
        tutorDto.setCoursesTeaching(user.getCoursesRegisteredFor());
        return tutorDto;
    }
}
