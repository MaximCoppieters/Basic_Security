package be.pxl.basic_security.service;


import be.pxl.basic_security.model.Message;
import be.pxl.basic_security.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);

    void sendMessage(Message message);
}
