package be.pxl.basic_security.model;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private String encryptedMessageFileName;

    private String encryptedAesKeyFileName;

    private String hashedMessageFileName;

    // Does not get stored in the database
    @Transient
    private String content;

    public Message() { }

    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getEncryptedMessageFileName() {
        return encryptedMessageFileName;
    }

    public String getHashedMessageFileName() {
        return hashedMessageFileName;
    }

    public void setHashedMessageFileName(String hashedMessageFileName) {
        this.hashedMessageFileName = hashedMessageFileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEncryptedMessageFileName(String messageFileName) {
        this.encryptedMessageFileName = messageFileName;
    }

    public String getEncryptedAesKeyFileName() {
        return encryptedAesKeyFileName;
    }

    public void setEncryptedAesKeyFileName(String encryptedAesKeyFileName) {
        this.encryptedAesKeyFileName = encryptedAesKeyFileName;
    }
}
