package be.pxl.basic_security.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
