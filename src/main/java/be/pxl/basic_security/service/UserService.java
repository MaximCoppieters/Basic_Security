package be.pxl.basic_security.service;


import be.pxl.basic_security.model.Message;
import be.pxl.basic_security.model.User;

import java.util.List;

public interface UserService {
    void save(User user);
    List<User> getAll();
    User findByUsername(String username);
}
