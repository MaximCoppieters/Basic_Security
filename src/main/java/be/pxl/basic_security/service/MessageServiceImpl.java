package be.pxl.basic_security.service;

import be.pxl.basic_security.model.Message;
import be.pxl.basic_security.model.User;
import be.pxl.basic_security.repository.MessageRepository;
import be.pxl.basic_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> findInboxFromUserName(String username) {
        User user = userRepository.findByUsername(username);

        return user.getInbox();
    }

    @Override
    public void sendMessage(Message message) {
        messageRepository.save(message);
    }
}
