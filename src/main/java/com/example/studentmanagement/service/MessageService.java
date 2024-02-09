package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;

import java.util.List;

public interface MessageService {
    Message save(String message,int toId,User fromUser);

    List<Message> myMessages(int id);
}
