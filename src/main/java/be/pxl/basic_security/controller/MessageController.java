package be.pxl.basic_security.controller;

import be.pxl.basic_security.model.Message;
import be.pxl.basic_security.model.User;
import be.pxl.basic_security.service.FileService;
import be.pxl.basic_security.service.MessageService;
import be.pxl.basic_security.service.SecurityService;
import be.pxl.basic_security.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
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
                .filter(this::isPersonalMessage)
                .filter(message -> message.getReceiver().getUsername().equals(currentUser.getUsername()))
                .collect(Collectors.toList());

        List<Message> outbox = currentUser.getOutbox()
                .stream()
                .filter(this::isPersonalMessage)
                .filter(message -> message.getReceiver().getUsername().equals(correspondentName))
                .collect(Collectors.toList());

        inbox.addAll(outbox);
        decryptMessages(inbox);

        model.addAttribute("activeUser", activeUsername);
        model.addAttribute("users", correspondents);
        model.addAttribute("messages", inbox);
        model.addAttribute("correspondent", currentUser.getCorrespondent());

        return "personal-chat";
    }

    public boolean isPersonalMessage(Message message) {
        return !message.isGroupChat() && message.getReceiver() != null;
    }

    @GetMapping("/group-chat")
    public String loadGroupChatPage
            (Model model) throws IOException, NoSuchAlgorithmException {
        String activeUsername = getActiveUserName();
        User currentUser = userService.findByUsername(activeUsername);
        currentUser.updateLastOnline();
        List<User> correspondents = getCorrespondentsOf(activeUsername);


        List<Message> groupChatMessages = messageService.findGroupMessages();

        decryptMessages(groupChatMessages);

        model.addAttribute("activeUser", activeUsername);
        model.addAttribute("users", correspondents);
        model.addAttribute("messages", groupChatMessages);

        return "group-chat";
    }

    private void decryptMessages(List<Message> messages) throws IOException, NoSuchAlgorithmException {
        for (Message message : messages) {
            securityService.decryptDiffieHellman(message);
        }
    }

    private List<User> getCorrespondentsOf(String username) {
        return userService.getAll()
                .stream()
                .filter(user -> !user.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    @PostMapping("group-chat")
    public String sendGroupMessage(@RequestParam("messageContent") String messageContent,
                        @RequestParam(value = "correspondent-name", required = false) String correspondentName,
                        @RequestParam(value= "appendix", required = false) MultipartFile appendix) throws IOException, NoSuchAlgorithmException {
        String senderName = getActiveUserName();
        User sender = userService.findByUsername(senderName);
        Message message = new Message(sender, messageContent);
        message.setGroupChat(true);
        if (correspondentName != null) {
            message.setReceiver(userService.findByUsername(correspondentName));
        } else {
            message.setEncryptMessage(false);
        }

        setMessageAppendixIfPresent(message, appendix);

        securityService.encryptDiffieHellman(message);

        sender.getOutbox().add(message);

        messageService.sendMessage(message);

        return "redirect:/group-chat";
    }

    @PostMapping({"/", "personal-chat" })
    public String sendPersonalMessage(@RequestParam("messageContent") String messageContent,
                                      @RequestParam(value = "correspondent-name", required = false) String correspondentName,
                                      @RequestParam(value = "appendix", required = false) MultipartFile appendix)
                       throws IOException, NoSuchAlgorithmException {
        String senderName = getActiveUserName();
        User sender = userService.findByUsername(senderName);
        User receiver = userService.findByUsername(correspondentName);
        Message message = new Message(sender, receiver, messageContent);

        setMessageAppendixIfPresent(message, appendix);

        securityService.encryptDiffieHellman(message);

        sender.getOutbox().add(message);
        receiver.getInbox().add(message);

        messageService.sendMessage(message);

        return "redirect:/personal-chat?correspondent-name=" + correspondentName;
    }

    private void setMessageAppendixIfPresent(Message message, MultipartFile appendix) throws IOException {
        if (appendix == null || appendix.isEmpty()) return;

        Path appendixPath = fileService.getPublicFilePathOf(appendix.getOriginalFilename());
        appendix.transferTo(appendixPath);
        message.setAppendix(appendixPath);
        message.setAppendixFileName(appendix.getOriginalFilename());
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
