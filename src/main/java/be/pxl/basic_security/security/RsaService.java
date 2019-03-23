package be.pxl.basic_security.security;


import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Service
public class RsaService {
    private KeyPairGenerator keyPairGenerator;

    public RsaService() throws NoSuchAlgorithmException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
    }

    public KeyPair generateKeyPair() {
        return keyPairGenerator.generateKeyPair();
    }
}
