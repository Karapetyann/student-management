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
    public String sendMessagePage(@AuthenticationPrincipal SpringUser springUser, @PathVariable("studentId") int id, ModelMap modelMap) {
        User fromUser = springUser.getUser();
        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()){
            User toUser = byId.get();
            modelMap.addAttribute("toUser", toUser);
        }
        modelMap.addAttribute("fromUser",fromUser);
        return "message";
    }

    @PostMapping("/message/send")
    public String sendMessage(@RequestParam("message") String message,
                              @RequestParam("toUserId") int toUserId,
                              @RequestParam("fromUserId") int fromUserId) {
        Optional<User> byId = userService.findById(toUserId);
        Optional<User> byId1 = userService.findById(fromUserId);
        User toUser = null;
        if (byId.isPresent()) {
            toUser = byId.get();
        }
        User fromUser = null;
        if (byId1.isPresent()) {
            fromUser = byId1.get();
        }
        if (toUser != null && fromUser != null) {
            Date date = new Date();
            Message newMessage = new Message(1, message, fromUser, toUser, date);
            messageService.save(newMessage);
            return "redirect:/user/home";
        } else {
            return "user";
        }
    }


    @GetMapping("/message/list")
    public String myMessages(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        User user = springUser.getUser();
        List<Message> messages = messageService.myMessages(user);
        if (messages != null) {
            modelMap.addAttribute("messages", messages);
        }
        return "myMessages";
    }

}
