package be.pxl.basic_security.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private LocalDateTime lastOnline;

    private String color;

    @Transient
    private String passwordConfirm;

    @Transient
    private User correspondent;

    @Lob
    @Column(columnDefinition="LONGTEXT")
    private String privateKey;

    @Lob
    @Column(columnDefinition="LONGTEXT")
    private String publicKey;

    @OneToMany(fetch = FetchType.EAGER, targetEntity=Message.class)
    private List<Message> inbox;

    @OneToMany(targetEntity=Message.class)
    private List<Message> outbox;

    @ManyToMany
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setLastOnline(LocalDateTime lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Message> getInbox() {
        return inbox;
    }

    public void setInbox(List<Message> inbox) {
        this.inbox = inbox;
    }

    public List<Message> getOutbox() {
        return outbox;
    }

    public void setOutbox(List<Message> outbox) {
        this.outbox = outbox;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public User getCorrespondent() {
        return correspondent;
    }

    public String getLastOnline() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
         if (lastOnline != null) {
            return lastOnline.format(timeFormatter);
        }
        return "";
    }

    public void updateLastOnline() {
        lastOnline = LocalDateTime.now();
    }

    public void setCorrespondent(User correspondent) {
        this.correspondent = correspondent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

}
