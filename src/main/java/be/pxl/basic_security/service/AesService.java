package be.pxl.basic_security.service;

import javax.crypto.SecretKey;
import java.io.File;
import java.security.Key;

public interface AesService {
    String encrypt(String message, SecretKey key);
    String decrypt(String secretMessage, SecretKey key);
    SecretKey generateKey();
    SecretKey extractKeyFromFile(File file);
    byte[] getEncodedKey(Key key);
    Key getDecodedKey(byte[] encodedKey);
}
