package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.MessageRepository;
import com.example.studentmanagement.service.MessageService;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final UserService userService;
    private final MessageRepository messageRepository;

    @Override
    public Message save(String message,int toId,User fromUser) {
        Optional<User> byId = userService.findById(toId);
        User toUser = null;
        if (byId.isPresent()) {
            toUser = byId.get();
        }
        if (toUser != null) {
            Date date = new Date();
            Message newMessage = new Message(0, message, fromUser, toUser, date);
            return messageRepository.save(newMessage);
        }
       return null;
    }

    @Override
    public List<Message> myMessages(int id) {
        return messageRepository.findAllByFrom_Id(id);
    }
}
