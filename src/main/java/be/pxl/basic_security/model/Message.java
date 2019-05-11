package be.pxl.basic_security.model;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "message")
public class Message implements Comparable<Message> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private LocalDateTime timeSent;
    private String encryptedMessageFileName;
    private String encryptedAppendixFileName;
    private String encryptedAesKeyFileName;
    private String hashedMessageFileName;
    private String hashedAppendixFileName;

    @Transient
    private Path appendix;
    private String appendixFileName;

    private boolean groupChat;

    // Does not get stored in the database
    @Transient
    private String content;

    private int fileId;

    public Message() { }

    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public String getAppendixFileName() {
        return appendixFileName;
    }

    public String getPublicAppendixFilePath() {
        return Paths.get("resources", "chat", appendixFileName).toString();
    }

    public void setAppendixFileName(String appendixFileName) {
        this.appendixFileName = appendixFileName;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public boolean isGroupChat() {
        return groupChat;
    }

    public void setGroupChat(boolean groupChat) {
        this.groupChat = groupChat;
    }

    public Path getAppendix() {
        return appendix;
    }

    public void setAppendix(Path appendix) {
        this.appendix = appendix;
    }

    public String getEncryptedAppendixFileName() {
        return encryptedAppendixFileName;
    }

    public void setEncryptedAppendixFileName(String encryptedAppendixFileName) {
        this.encryptedAppendixFileName = encryptedAppendixFileName;
    }

    public String getHashedAppendixFileName() {
        return hashedAppendixFileName;
    }

    public void setHashedAppendixFileName(String hashedAppendixFileName) {
        this.hashedAppendixFileName = hashedAppendixFileName;
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

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public String getDaySent() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE");

        return timeSent.format(formatter);
    }

    @Override
    public int compareTo(Message o) {
        return this.getTimeSent().isBefore(o.getTimeSent()) ? -1 : 1;
    }
}
