package be.pxl.basic_security.service;

import be.pxl.basic_security.model.Message;

import javax.crypto.NoSuchPaddingException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.List;

public interface RsaService {
    KeyPair generateKeyPair();
    String encrypt(String content, Key publicKey);
    String getEncodedKey(Key key);
    Key getDecodedKey(String encodedKey, Class<? extends Key> keyType);
    String decrypt(String content, Key privateKey);
}
