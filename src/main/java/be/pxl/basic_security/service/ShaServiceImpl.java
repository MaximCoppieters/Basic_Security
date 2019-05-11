package be.pxl.basic_security.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ShaServiceImpl implements ShaService {
    public String getHashOf(String content) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digester.digest(content.getBytes(StandardCharsets.UTF_8));
        return new String(encodedHash);
    }

    @Override
    public byte[] getHashOf(byte[] content) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        return digester.digest(content);
    }
}
