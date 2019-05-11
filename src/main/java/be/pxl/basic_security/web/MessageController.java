package be.pxl.basic_security.web;

import be.pxl.basic_security.model.Message;
import be.pxl.basic_security.model.User;
import be.pxl.basic_security.service.FileService;
import be.pxl.basic_security.service.MessageService;
import be.pxl.basic_security.service.SecurityService;
import be.pxl.basic_security.service.UserService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MessageController {
    private final UserService userService;
    private final MessageService messageService;
    private final SecurityService securityService;
    private final FileService fileService;

    public MessageController(UserService userService,
                             MessageService messageService,
                             SecurityService securityService,
                             FileService fileService) {
        this.userService = userService;
        this.messageService = messageService;
        this.securityService = securityService;
        this.fileService = fileService;
    }

    @GetMapping({"/", "/personal-chat" })
    public String loadChatPage
            (Model model, @RequestParam(value = "correspondent-name", required = false)
                    String correspondentName) throws IOException, NoSuchAlgorithmException {
        String activeUsername = getActiveUserName();
        List<User> correspondents = getCorrespondentsOf(activeUsername);
        User currentUser = userService.findByUsername(activeUsername);
        currentUser.updateLastOnline();

        if (correspondentName != null) {
            User correspondent = userService.findByUsername(correspondentName);
            currentUser.setCorrespondent(correspondent);
        } else {
            if (currentUser.getCorrespondent() == null && correspondents.size() > 0) {
                currentUser.setCorrespondent(correspondents.get(0));
            }
        }

        List<Message> inbox = currentUser.getInbox()
                .stream()
                .filter(message -> message.getReceiver().getUsername().equals(currentUser.getUsername()))
                .collect(Collectors.toList());

        for (Message message : inbox) {
            securityService.decryptDiffieHellman(message);
        }

        List<Message> outbox = currentUser.getOutbox()
                .stream()
                .filter(message -> message.getReceiver().getUsername().equals(correspondentName))
                .collect(Collectors.toList());

        for (Message message : outbox) {
            securityService.decryptDiffieHellman(message);
        }

        inbox.addAll(outbox);

        model.addAttribute("activeUser", activeUsername);
        model.addAttribute("users", correspondents);
        model.addAttribute("messages", inbox);
        model.addAttribute("correspondent", currentUser.getCorrespondent());

        return "personal-chat";
    }

    @GetMapping("/group-chat")
    @ResponseBody
    public FileSystemResource downloadAppendix(@Param(value="fileId") int fileId) {
        return new FileSystemResource("");
    }

    private List<User> getCorrespondentsOf(String username) {
        return userService.getAll()
                .stream()
                .filter(user -> !user.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    @PostMapping({"/", "personal-chat" })
    public String send(@RequestParam("messageContent") String messageContent,
                       @RequestParam(value = "correspondent-name", required = false) String correspondentName,
                       @RequestParam("appendix") MultipartFile appendix)
                       throws IOException, NoSuchAlgorithmException {
        String senderName = getActiveUserName();
        User sender = userService.findByUsername(senderName);
        User receiver = userService.findByUsername(correspondentName);
        Message message = new Message(sender, receiver, messageContent);

        Path appendixPath = fileService.getPublicFilePathOf(appendix.getOriginalFilename());
        appendix.transferTo(appendixPath);
        message.setAppendix(appendixPath);
        message.setAppendixFileName(appendix.getOriginalFilename());

        securityService.encryptDiffieHellman(message);

        sender.getOutbox().add(message);
        receiver.getInbox().add(message);

        messageService.sendMessage(message);

        return "redirect:/personal-chat?correspondent-name=" + correspondentName;
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
