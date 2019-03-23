package be.pxl.basic_security.service;


import be.pxl.basic_security.model.Message;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

@Service
public class RsaServiceImpl implements RsaService {
    private KeyPairGenerator keyPairGenerator;

    public RsaServiceImpl() throws NoSuchAlgorithmException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
    }

    public KeyPair generateKeyPair() {
        return keyPairGenerator.generateKeyPair();
    }

    @Override
    public String encrypt(String content, Key publicKey) {
        String encryptedContent = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedContent = new String(cipher.doFinal(content.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedContent;
    }

    @Override
    public String getEncodedKey(Key key) {
        return Base64.encodeBase64String(key.getEncoded());
    }

    @Override
    public Key getDecodedKey(String encodedKey, Class<? extends Key> keyType) {
        Key key = null;
        try {
            byte[] keyBytes = Base64.decodeBase64(encodedKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            if (keyType == PublicKey.class) {
                key = keyFactory.generatePublic(keySpec);
            } else {
                key = keyFactory.generatePrivate(keySpec);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return key;
    }

    @Override
    public String decrypt(String encryption, Key privateKey) {
        String decryptedContent = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedContent = new String(cipher.doFinal(encryption.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedContent;
    }
}
