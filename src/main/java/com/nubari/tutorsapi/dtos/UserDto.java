package com.nubari.tutorsapi.dtos;

import com.nubari.tutorsapi.models.Class;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;


    public User unpackDto() {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public UserDto packDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        return userDto;
    }
}
