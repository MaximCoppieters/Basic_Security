package be.pxl.basic_security.controller;

import be.pxl.basic_security.model.User;
import be.pxl.basic_security.service.*;
import be.pxl.basic_security.validator.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;

@Controller
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserValidator userValidator;
    private final RsaService rsaService;

    public UserController(UserService userService, AuthenticationService authenticationService, UserValidator userValidator, RsaService rsaService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userValidator = userValidator;
        this.rsaService = rsaService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        KeyPair keypair = rsaService.generateKeyPair();

        String privateUserKey = rsaService.getEncodedKey(keypair.getPrivate());
        String publicUserKey = rsaService.getEncodedKey(keypair.getPublic());

        userForm.setPrivateKey(privateUserKey);
        userForm.setPublicKey(publicUserKey);

        userForm.setColor(userService.generateUserColor());
        userService.save(userForm);

        authenticationService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/login";
    }


    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "/login";
    }
}
