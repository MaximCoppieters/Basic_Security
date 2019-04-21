package be.pxl.basic_security.service;


import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class RsaServiceImpl implements RsaService {
    private KeyPairGenerator keyPairGenerator;

    public RsaServiceImpl() throws NoSuchAlgorithmException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
    }

    public KeyPair generateKeyPair() {
        return keyPairGenerator.generateKeyPair();
    }

    @Override
    public byte[] encrypt(byte[] content, Key publicKey) {
        byte[] encryptedContent = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedContent = cipher.doFinal(content);
            System.out.println("Length of RSA encrypted public key cypher: " + encryptedContent.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedContent;
    }

    @Override
    public byte[] decrypt(byte[] content, Key privateKey) {
        byte[] decryptedContent = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedContent = cipher.doFinal(content);
            System.out.println("Length of RSA decrypted private key cypher: " + decryptedContent.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedContent;
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
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            if (keyType == PublicKey.class) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                key = keyFactory.generatePublic(keySpec);
            } else {
                PKCS8EncodedKeySpec keySpec =
                        new PKCS8EncodedKeySpec(keyBytes);
                key = keyFactory.generatePrivate(keySpec);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return key;
    }
}
