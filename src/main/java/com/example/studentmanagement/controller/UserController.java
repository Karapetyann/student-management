package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.repository.LessonRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.security.SpringUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserControllerAdvice userControllerAdvice;

    @Autowired
    private LessonRepository lessonRepository;
    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @GetMapping("/user/student")
    public String studentPage(ModelMap modelMap ) {
        List<User> users = userRepository.findByUserType(UserType.STUDENT);
        modelMap.put("users", users);
        return "user";
    }
    @GetMapping("/user/teacher")
    public String teacherPage(ModelMap modelMap ) {
        List<User> users = userRepository.findByUserType(UserType.TEACHER);
        modelMap.put("users", users);
        return "user";
    }

    @GetMapping("/user/add")
    public String addUserPage(ModelMap modelMap) {
        modelMap.addAttribute("lessons", lessonRepository.findAll());
        return "addUser";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute User user, @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                File file = new File(uploadDirectory, picName);
                multipartFile.transferTo(file);
                user.setPicName(picName);
            }
            userRepository.save(user);
            if (user.getUserType() == UserType.TEACHER) {
                return "redirect:/user/teacher?msg=User Added";
            }
                return "redirect:/user/student?msg=User Added";
        } else {
            return "redirect:/user/register?msg=Email already in use";
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return "redirect:/user";
    }

    @GetMapping("/user/update/{id}")
    public String updateUserPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<User> byId = userRepository.findById(id);
        List<Lesson> lessons = lessonRepository.findAll();
        if (byId.isPresent()) {
            User user = byId.get();
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("lessons", lessons);
            return "updateUser";
        }
        return "user";
    }

    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute User user, @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && multipartFile.getSize() > 0) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            user.setPicName(picName);
        } else {
            Optional<User> byId = userRepository.findById(user.getId());
            if (byId.isPresent()) {
                User userById = byId.get();
                user.setPicName(userById.getPicName());
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/user";
    }

    @GetMapping("/user/register")
    public String userRegisterPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        return "register";
    }

    @PostMapping("/user/register")
    public String userRegister(@ModelAttribute User user, @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                File file = new File(uploadDirectory, picName);
                multipartFile.transferTo(file);
                user.setPicName(picName);
            }
            userRepository.save(user);
            return "redirect:/home?msg=User Registered";
        } else {
            return "redirect:/user/register?msg=Email already in use";
        }
    }
}
