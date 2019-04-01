package be.pxl.basic_security.web;

import be.pxl.basic_security.model.User;
import be.pxl.basic_security.service.*;
import be.pxl.basic_security.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.util.Base64;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private RsaService rsaService;

    @Autowired
    private AesService aesService;

    @Autowired
    private ShaService shaService;


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

        userService.save(userForm);

        authenticationService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }
}
