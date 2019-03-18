package be.pxl.basic_security.service;

import be.pxl.basic_security.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> findInboxFromUserName(String username);
    void sendMessage(Message message);
}
