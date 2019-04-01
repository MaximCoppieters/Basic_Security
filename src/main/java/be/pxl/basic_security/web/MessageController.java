package be.pxl.basic_security.web;

import be.pxl.basic_security.model.Message;
import be.pxl.basic_security.model.User;
import be.pxl.basic_security.service.MessageService;
import be.pxl.basic_security.service.SecurityService;
import be.pxl.basic_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/sendmessage")
    public String loadSendMessagePage(Model model) {
        List<User> users = userService.getAll();

        model.addAttribute("users", users);
        return "sendmessage";
    }

    @PostMapping("/sendmessage")
    public String send(@RequestParam("messageContent") String messageContent,
                       @RequestParam("receiverName") String receiverName) throws IOException, NoSuchAlgorithmException {
        String senderName = getActiveUserName();
        User sender = userService.findByUsername(senderName);
        User receiver = userService.findByUsername(receiverName);
        Message message = new Message(sender, receiver, messageContent);

        securityService.encryptDiffieHellman(message);

        sender.getOutbox().add(message);
        receiver.getInbox().add(message);

        messageService.sendMessage(message);

        return "redirect:/welcome";
    }

    @GetMapping("/readmessages")
    public String readMessages(Model model) throws IOException, NoSuchAlgorithmException {
        String activeUsername = getActiveUserName();

        System.out.println("active user: " + activeUsername);
        List<Message> userInbox = messageService.findInboxFromUserName(activeUsername);

        for (Message message : userInbox) {
            securityService.decryptDiffieHellman(message);
        }

        model.addAttribute("messages", userInbox);

        return "readmessages";
    }

    private String getActiveUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
