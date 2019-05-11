package be.pxl.basic_security.service;

import be.pxl.basic_security.model.Message;
import be.pxl.basic_security.model.User;
import be.pxl.basic_security.repository.MessageRepository;
import be.pxl.basic_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> findInboxFromUserName(String username) {
        User user = userRepository.findByUsername(username);

        return user.getInbox();
    }

    @Override
    public void sendMessage(Message message) {
        message.setTimeSent(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Override
    public List<Message> findGroupMessages() {
        return messageRepository.findAll()
                .stream()
                .filter(Message::isGroupChat)
                .collect(Collectors.toList());
    }

    @Override
    public Message getMessageByFileId(int fileId) {
        return messageRepository.findAll()
                .stream()
                .filter(message -> message.getFileId() == fileId)
                .findFirst().get();
    }
}
