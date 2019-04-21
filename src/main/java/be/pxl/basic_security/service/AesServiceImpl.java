package be.pxl.basic_security.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.util.Base64;

@Service
public class AesServiceImpl implements AesService {
    private static KeyGenerator keygen;

    static {
        try {
            keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String message, SecretKey key) {
        String secretMessage = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            secretMessage =  Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return secretMessage;
    }

    public String decrypt(String secretMessage, SecretKey key) {
        String decryptedMessage = "";
        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedMessage = new String(
                    cipher.doFinal(Base64.getDecoder().decode(secretMessage)));
        }
            catch (Exception e) {
                e.printStackTrace();
        }
        return decryptedMessage;
    }


    public SecretKey generateKey() { return keygen.generateKey(); }
    public SecretKey extractKeyFromFile(File file){return null;}

    @Override
    public SecretKey getDecodedKey(byte[] encodedKey) {
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}
