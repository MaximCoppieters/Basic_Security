package be.pxl.basic_security.service;

public interface AuthenticationService {
    String findLoggedInUsername();
    void autoLogin(String username, String password);
}
