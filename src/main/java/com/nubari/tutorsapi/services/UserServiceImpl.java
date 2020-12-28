package com.nubari.tutorsapi.services;

import com.nubari.tutorsapi.dtos.StudentDto;
import com.nubari.tutorsapi.dtos.TutorDto;
import com.nubari.tutorsapi.exceptions.CourseLimitReachedException;
import com.nubari.tutorsapi.exceptions.CourseNotFoundException;
import com.nubari.tutorsapi.exceptions.StudentNotFoundException;
import com.nubari.tutorsapi.exceptions.TutorNotFoundException;
import com.nubari.tutorsapi.models.Course;
import com.nubari.tutorsapi.models.User;
import com.nubari.tutorsapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    UserRepository userRepository;

//    @Autowired
//    ClassService classService;

    @Autowired
    CourseService courseService;

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
    public List<StudentDto> getAllStudents() {
        List<StudentDto> studentDtos = new ArrayList<>();
        List<User> students = getAllExistingStudents();
        for (User student : students) {
            StudentDto studentDto = new StudentDto();
            studentDtos.add(studentDto.packDto(student));
        }
        return studentDtos;
    }

    private List<User> getAllExistingStudents() {
        List<User> users = userRepository.findAll();
        List<User> students = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType().equals("student")) {
                students.add(user);
            }
        }
        return students;
    }

    @Override
    public List<TutorDto> getAllTutors() {
        List<TutorDto> tutorDtos = new ArrayList<>();
        List<User> tutors = getAllExistingTutors();
        for (User tutor : tutors) {
            TutorDto tutorDto = new TutorDto();
            tutorDtos.add(tutorDto.packDto(tutor));
        }
        return tutorDtos;
    }

    private List<User> getAllExistingTutors() {
        List<User> users = userRepository.findAll();
        List<User> tutors = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType().equals("tutor")) {
                tutors.add(user);
            }
        }
        return tutors;
    }

    @Override
    public StudentDto getStudentDetails(String studentId) throws StudentNotFoundException {
        StudentDto studentDto = new StudentDto();
        return studentDto.packDto(findStudentById(studentId));
    }

    @Override
    public TutorDto getTutorDetails(String tutorId) throws TutorNotFoundException {
        TutorDto tutorDto = new TutorDto();
        return tutorDto.packDto(findATutorById(tutorId));
    }

    @Override
    public StudentDto updateStudentDetails(String studentId, StudentDto studentDto) throws StudentNotFoundException {
        User studentToUpdate = findStudentById(studentId);
        if (!studentDto.getUsername().equals("")) {
            studentToUpdate.setUsername(studentDto.getUsername());
        }
        if (!studentDto.getFirstname().equals("")) {
            studentToUpdate.setFirstname(studentDto.getFirstname());
        }
        if (!studentDto.getLastname().equals("")) {
            studentToUpdate.setLastname(studentDto.getLastname());
        }
        if (!studentDto.getEmail().equals("")) {
            studentToUpdate.setEmail(studentDto.getEmail());
        }
        User updatedStudent = updateStudent(studentToUpdate);
        StudentDto returnValue = new StudentDto();
        return returnValue.packDto(updatedStudent);
    }

    private User updateStudent(User updatedStudent) {
        return userRepository.save(updatedStudent);
    }

    @Override
    public TutorDto updateTutorDetails(String tutorId, TutorDto tutorDto) throws TutorNotFoundException {
        User tutorToUpdate = findATutorById(tutorId);
        if (!tutorDto.getUsername().equals("")) {
            tutorToUpdate.setUsername(tutorDto.getUsername());
        }
        if (!tutorDto.getFirstname().equals("")) {
            tutorToUpdate.setFirstname(tutorDto.getFirstname());
        }
        if (!tutorDto.getLastname().equals("")) {
            tutorToUpdate.setLastname(tutorDto.getLastname());
        }
        if (!tutorDto.getEmail().equals("")) {
            tutorToUpdate.setEmail(tutorDto.getEmail());
        }
        User updatedTutor = updateTutor(tutorToUpdate);
        TutorDto returnValue = new TutorDto();
        return returnValue.packDto(updatedTutor);
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
        for (Course course: coursesTutorIsTaking){
            courseService.removeTutorFromCourse(tutor.getId(),course.getId());
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
    public void scheduleAClassForACourse(String tutorId, String courseId) {

    }

    @Override
    public void cancelAClass(String tutorId, String courseId) {

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
}
