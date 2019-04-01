package be.pxl.basic_security.service;

import be.pxl.basic_security.model.Message;

import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.List;

public interface RsaService {
    KeyPair generateKeyPair();
    byte[] encrypt(byte[] content, Key key);
    byte[] decrypt(byte[] content, Key key);
    String getEncodedKey(Key key);
    Key getDecodedKey(String encodedKey, Class<? extends Key> keyType);
}
