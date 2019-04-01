package be.pxl.basic_security.service;

import be.pxl.basic_security.model.Message;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface SecurityService {
    void encryptDiffieHellman(Message message) throws IOException, NoSuchAlgorithmException;
    void decryptDiffieHellman(Message message) throws IOException, NoSuchAlgorithmException;
}
