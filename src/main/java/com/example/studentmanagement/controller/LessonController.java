package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.repository.LessonRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.security.SpringUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class LessonController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping("/lesson")
    public String lessonPage(ModelMap modelMap) {
        List<Lesson> lessons = lessonRepository.findAll();
        modelMap.put("lessons", lessons);
        return "lesson";
    }

    @GetMapping("/lesson/add")
    public String addLessonPage(ModelMap modelMap) {
        List<User> teachers = userRepository.findByUserType(UserType.TEACHER);
        if (teachers != null && !teachers.isEmpty()) {
            modelMap.addAttribute("teachers", teachers);
            return "addLesson";
        }
        return "addLesson";
    }

    @PostMapping("/lesson/add")
    public String addLesson(@ModelAttribute Lesson lesson, @RequestParam("teacherId") int teacherId) {
        Optional<User> byId = userRepository.findById(teacherId);
        byId.ifPresent(lesson::setUser);
        lessonRepository.save(lesson);
        return "redirect:/lesson";
    }


    @Transactional
    @GetMapping("/lesson/delete/{id}")
    public String deleteLesson(@PathVariable("id") int id) {
        Optional<Lesson> byId = lessonRepository.findById(id);
        if (byId.isPresent()) {
            Lesson lesson = byId.get();
            userRepository.deleteUserByLesson(lesson);
        }
        lessonRepository.deleteById(id);
        return "redirect:/lesson";
    }

    @GetMapping("/lesson/update/{id}")
    public String updateLessonPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Lesson> byId = lessonRepository.findById(id);
        List<User> teachers = userRepository.findByUserType(UserType.TEACHER);
        if (byId.isPresent()) {
            Lesson lesson = byId.get();
            modelMap.addAttribute("lesson", lesson);
            modelMap.addAttribute("teachers", teachers);
            return "updateLesson";
        }
        return "lesson";
    }

    @PostMapping("/lesson/update")
    public String updateUser(@ModelAttribute Lesson lesson){
        lessonRepository.save(lesson);
        return "redirect:/lesson";
    }
}

