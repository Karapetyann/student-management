package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.security.SpringUser;
import com.example.studentmanagement.service.MessageService;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/message/send/{studentId}")
    public String sendMessagePage(@PathVariable("studentId") int id, ModelMap modelMap) {
        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()){
            User toUser = byId.get();
            modelMap.addAttribute("toUser", toUser);
            return "message";
        }
        return "user";
    }

    @PostMapping("/message/send")
    public String sendMessage(@RequestParam("message") String message,
                              @RequestParam("toUserId") int toUserId,
                              @AuthenticationPrincipal SpringUser springUser) {
        User fromUser = springUser.getUser();
        Message addMessage = messageService.save(message, toUserId, fromUser);
        if (addMessage == null){
            return "redirect:/user/home";
        } else {
            return "redirect:/user/home";
        }
    }


    @GetMapping("/message/list")
    public String myMessages(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        User user = springUser.getUser();
        List<Message> messages = messageService.myMessages(user.getId());
        if (messages != null) {
            modelMap.addAttribute("messages", messages);
        }
        return "myMessages";
    }

}
