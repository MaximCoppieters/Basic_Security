package be.pxl.basic_security.service;


import be.pxl.basic_security.model.User;

import java.security.KeyPair;
import java.util.List;

public interface UserService {
    void save(User user);
    List<User> getAll();
    User findByUsername(String username);
    KeyPair generateKeypair();
}
