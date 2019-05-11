package be.pxl.basic_security.service;

import java.security.NoSuchAlgorithmException;

public interface ShaService {
    String getHashOf(String content) throws NoSuchAlgorithmException;
    byte[] getHashOf(byte[] content) throws NoSuchAlgorithmException;
}
