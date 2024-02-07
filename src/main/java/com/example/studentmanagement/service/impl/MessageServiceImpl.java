package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.MessageRepository;
import com.example.studentmanagement.service.MessageService;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final UserService userService;
    private final MessageRepository messageRepository;

    @Override
    public Message save(Message message) {
       return messageRepository.save(message);
    }

    @Override
    public List<Message> myMessages(User user) {
        return messageRepository.findAllByFrom(user);
    }
}
