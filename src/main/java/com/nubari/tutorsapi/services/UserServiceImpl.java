package com.nubari.tutorsapi.services;


import com.nubari.tutorsapi.dtos.UserDto;
import com.nubari.tutorsapi.exceptions.*;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.Role;
import com.nubari.tutorsapi.models.User;
import com.nubari.tutorsapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassService classService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CourseService courseService;

    @Autowired
    RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadAUserByUsername(username);
    }

    private UserDetails loadAUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    @Override
    public List<UserDto> getAllStudents() throws UserRoleNotFoundException {
        List<UserDto> userDtos = new ArrayList<>();
        List<User> students = getAllExistingStudents();
        for (User student : students) {
            UserDto userDto = new UserDto();
            userDtos.add(userDto.packDto(student));
        }
        return userDtos;
    }

    private List<User> getAllExistingStudents() throws UserRoleNotFoundException {
        List<User> users = userRepository.findAll();
        List<User> students = new ArrayList<>();
        Role studentRole = roleService.findByName("Student");
        for (User user : users) {
            if (user.getRoles().contains(studentRole)) {
                students.add(user);
            }
        }
        return students;
    }

    @Override
    public List<UserDto> getAllTutors() throws UserRoleNotFoundException {
        List<UserDto> tutorDtos = new ArrayList<>();
        List<User> tutors = getAllExistingTutors();
        for (User tutor : tutors) {
            UserDto tutorDto = new UserDto();
            tutorDtos.add(tutorDto.packDto(tutor));
        }
        return tutorDtos;
    }

    private List<User> getAllExistingTutors() throws UserRoleNotFoundException {
        List<User> users = userRepository.findAll();
        List<User> tutors = new ArrayList<>();
        Role tutorRole = roleService.findByName("Tutor");
        for (User user : users) {
            if (user.getRoles().contains(tutorRole)) {
                tutors.add(user);
            }
        }
        return tutors;
    }

    @Override
    public UserDto getStudentDetails(String studentId) throws StudentNotFoundException {
        UserDto userDto = new UserDto();
        return userDto.packDto(findStudentById(studentId));
    }

    @Override
    public UserDto getTutorDetails(String tutorId) throws TutorNotFoundException {
        UserDto userDto = new UserDto();
        return userDto.packDto(findATutorById(tutorId));
    }

    @Override
    public UserDto updateStudentDetails(String studentId, UserDto userDto) throws StudentNotFoundException {
        User studentToUpdate = findStudentById(studentId);
        if (!userDto.getUsername().equals("")) {
            studentToUpdate.setUsername(userDto.getUsername());
        }
        if (!userDto.getFirstname().equals("")) {
            studentToUpdate.setFirstname(userDto.getFirstname());
        }
        if (!userDto.getLastname().equals("")) {
            studentToUpdate.setLastname(userDto.getLastname());
        }
        if (!userDto.getEmail().equals("")) {
            studentToUpdate.setEmail(userDto.getEmail());
        }
        User updatedStudent = updateStudent(studentToUpdate);
        return userDto.packDto(updatedStudent);
    }

    private User updateStudent(User updatedStudent) {
        return userRepository.save(updatedStudent);
    }

    @Override
    public UserDto updateTutorDetails(String tutorId, UserDto userDto) throws TutorNotFoundException {
        User tutorToUpdate = findATutorById(tutorId);
        if (!userDto.getUsername().equals("")) {
            tutorToUpdate.setUsername(userDto.getUsername());
        }
        if (!userDto.getFirstname().equals("")) {
            tutorToUpdate.setFirstname(userDto.getFirstname());
        }
        if (!userDto.getLastname().equals("")) {
            tutorToUpdate.setLastname(userDto.getLastname());
        }
        if (!userDto.getEmail().equals("")) {
            tutorToUpdate.setEmail(userDto.getEmail());
        }
        User updatedTutor = updateTutor(tutorToUpdate);
        return userDto.packDto(updatedTutor);
    }

    private User updateTutor(User updatedTutor) {
        return userRepository.save(updatedTutor);
    }

    @Override
    public void deleteStudent(String studentId) throws CourseNotFoundException, StudentNotFoundException {
        deleteAStudent(studentId);
    }

    private void deleteAStudent(String studentId) throws StudentNotFoundException, CourseNotFoundException {
        User student = findAStudentById(studentId);
        List<Course> coursesStudentIsCurrentRegisteredFor = student.getCoursesRegisteredFor();
        for (Course course : coursesStudentIsCurrentRegisteredFor) {
            courseService.removeStudentFromCourse(student.getId(), course.getId());
        }
        userRepository.delete(student);
    }

    @Override
    public void deleteTutor(String tutorId) throws CourseNotFoundException, TutorNotFoundException {
        deleteATutor(tutorId);
    }

    private void deleteATutor(String tutorId) throws CourseNotFoundException, TutorNotFoundException {
        User tutor = findATutorById(tutorId);
        List<Course> coursesTutorIsTaking = tutor.getCoursesRegisteredFor();
        for (Course course : coursesTutorIsTaking) {
            courseService.removeTutorFromCourse(tutor.getId(), course.getId());
        }
    }


    @Override
    public void registerStudentForCourse(String studentId, String courseId) throws CourseNotFoundException, CourseLimitReachedException, StudentNotFoundException {
        registerStudentForACourse(studentId, courseId);
    }

    private void registerStudentForACourse(String studentId, String courseId) throws StudentNotFoundException, CourseNotFoundException, CourseLimitReachedException {
        User student = findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);
        courseService.addStudentToCourse(student.getId(), course.getId());

    }

    @Override
    public void removeStudentFromCourse(String studentId, String courseId) throws CourseNotFoundException, StudentNotFoundException {
        removeAStudentFromACourse(studentId, courseId);
    }

    private void removeAStudentFromACourse(String studentId, String courseId) throws StudentNotFoundException, CourseNotFoundException {
        User student = findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);
        courseService.removeStudentFromCourse(student.getId(), course.getId());
    }

    @Override
    public void registerTutorForCourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException {
        registerATutorForACourse(tutorId, courseId);
    }

    private void registerATutorForACourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException {
        User tutor = findTutorById(tutorId);
        Course course = courseService.findCourseById(courseId);
        courseService.addTutorToCourse(tutor.getId(), course.getId());
    }

    @Override
    public void removeTutorFromCourse(String tutorId, String courseId) throws CourseNotFoundException, TutorNotFoundException {
        removeATutorFromACourse(tutorId, courseId);
    }

    private void removeATutorFromACourse(String tutorId, String courseId) throws TutorNotFoundException, CourseNotFoundException {
        courseService.removeTutorFromCourse(tutorId, courseId);

    }

    @Override
    public User findStudentById(String studentId) throws StudentNotFoundException {
        return findAStudentById(studentId);
    }

    private User findAStudentById(String studentId) throws StudentNotFoundException {
        Optional<User> userOptional = userRepository.findById(studentId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new StudentNotFoundException();
        }
    }

    @Override
    public User findTutorById(String tutorId) throws TutorNotFoundException {
        return findATutorById(tutorId);
    }

    private User findATutorById(String tutorId) throws TutorNotFoundException {
        Optional<User> userOptional = userRepository.findById(tutorId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new TutorNotFoundException();
        }
    }

    @Override
    public void saveUser(User user) {
        saveAUser(user);
    }

    private void saveAUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDto registerStudent(UserDto userDto) throws UserRoleNotFoundException, UsernameAlreadyExistsException, EmailAlreadyExistsException {
        User student = userDto.unpackDto();
        boolean usernameExists = checkIfUserWithThisUsernameExists(student.getUsername());
        boolean emailExists = checkIfUserWithThisEmailExists(student.getEmail());
        if (usernameExists) {
            throw new UsernameAlreadyExistsException();
        }
        if (emailExists) {
            throw new EmailAlreadyExistsException();
        }
        student.setPassword(bCryptPasswordEncoder.encode(student.getPassword()));
        Role role = roleService.findByName("Student");
        student.getRoles().add(role);
        User savedStudent = registerStudent(student);
        return userDto.packDto(savedStudent);
    }

    private User registerStudent(User student) {
        return userRepository.save(student);
    }

    @Override
    public UserDto registerTutor(UserDto userDto) throws UserRoleNotFoundException, UsernameAlreadyExistsException, EmailAlreadyExistsException {
        User tutor = userDto.unpackDto();
        boolean usernameExists = checkIfUserWithThisUsernameExists(tutor.getUsername());
        boolean emailExists = checkIfUserWithThisEmailExists(tutor.getEmail());
        if (usernameExists) {
            throw new UsernameAlreadyExistsException();
        }
        if (emailExists) {
            throw new EmailAlreadyExistsException();
        }
        tutor.setPassword(bCryptPasswordEncoder.encode(tutor.getPassword()));
        Role role = roleService.findByName("Tutor");
        tutor.getRoles().add(role);
        User savedTutor = registerTutor(tutor);
        return userDto.packDto(savedTutor);
    }

    private User registerTutor(User tutor) {
        return userRepository.save(tutor);
    }

    @Override
    public boolean checkIfUserWithThisUsernameExists(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent();
    }

    @Override
    public boolean checkIfUserWithThisEmailExists(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.isPresent();
    }

}
