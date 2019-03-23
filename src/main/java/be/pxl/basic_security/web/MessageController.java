package be.pxl.basic_security.web;

import be.pxl.basic_security.model.Message;
import be.pxl.basic_security.model.User;
import be.pxl.basic_security.service.MessageService;
import be.pxl.basic_security.service.RsaService;
import be.pxl.basic_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RsaService rsaService;

    @GetMapping("/sendmessage")
    public String loadSendMessagePage(Model model) {
        List<User> users = userService.getAll();

        model.addAttribute("users", users);
        return "sendmessage";
    }

    @PostMapping("/sendmessage")
    public String send(@RequestParam("messageContent") String messageContent,
                       @RequestParam("receiverName") String receiverName) {
        String senderName = getActiveUserName();
        User sender = userService.findByUsername(senderName);
        User receiver = userService.findByUsername(receiverName);

        Key receiverPublicKey = rsaService.getDecodedKey(receiver.getPublicKey(), PublicKey.class);

        String encryptedContent = rsaService.encrypt(messageContent, receiverPublicKey);
        Message message = new Message(encryptedContent, sender, receiver);

        sender.getOutbox().add(message);
        receiver.getInbox().add(message);

        messageService.sendMessage(message);

        return "redirect:/welcome";
    }

    @GetMapping("/readmessages")
    public String readMessages(Model model, String error, String logout) {
        String activeUsername = getActiveUserName();

        System.out.println("active user: " + activeUsername);
        User user = userService.findByUsername(activeUsername);
        List<Message> userInbox = messageService.findInboxFromUserName(activeUsername);

        Key userPrivateKey = rsaService.getDecodedKey(user.getPrivateKey(), PrivateKey.class);

        userInbox.forEach(message -> rsaService.decrypt(message.getContent(), userPrivateKey));


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
