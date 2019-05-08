package be.pxl.basic_security.service;

import be.pxl.basic_security.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Path;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SecurityServiceImpl implements SecurityService {
    private final RsaService rsaService;

    private final AesService aesService;

    private final ShaService shaService;

    private final FileService fileService;

    private ThreadLocalRandom rng = ThreadLocalRandom.current();
    private int randomFilePostfix;
    private Message currentMessage;

    public SecurityServiceImpl(RsaService rsaService, AesService aesService, ShaService shaService, FileService fileService) {
        this.rsaService = rsaService;
        this.aesService = aesService;
        this.shaService = shaService;
        this.fileService = fileService;
    }

    public void encryptDiffieHellman(Message message) throws IOException, NoSuchAlgorithmException {
        Key receiverPublicKey = rsaService.getDecodedKey(message.getReceiver().getPublicKey(), PublicKey.class);
        randomFilePostfix = rng.nextInt(0, 1000);
        currentMessage = message;

        SecretKey aesKey = aesService.generateKey();
        String encryptedMessage = aesService.encrypt(message.getContent(), aesKey);

        storeEncryptedAesKey(receiverPublicKey, aesKey);
        storeEncryptedMessage(encryptedMessage);
        storeEncryptedMessageHash(message.getContent(), aesKey);
    }

    private void storeEncryptedAesKey(Key receiverPublicKey, SecretKey aesKey) throws IOException {
        byte[] encodedAesKey = aesKey.getEncoded();
        byte[] encryptedAesKey = rsaService.encrypt(encodedAesKey, receiverPublicKey);
        String encryptedAesKeyFileName = formTextFileNameWith("aes_key", randomFilePostfix);
        writeToFileWithName(encryptedAesKey, encryptedAesKeyFileName);
        currentMessage.setEncryptedAesKeyFileName(encryptedAesKeyFileName);
    }

    private void storeEncryptedMessage(String encryptedMessage) throws IOException {
        String encryptedMessageFileName = formTextFileNameWith("encrypted_message", randomFilePostfix);
        writeToFileWithName(encryptedMessage, encryptedMessageFileName);
        currentMessage.setEncryptedMessageFileName(encryptedMessageFileName);
    }

    private void storeEncryptedMessageHash(String messageContent, SecretKey aesKey)
                throws IOException, NoSuchAlgorithmException {
        String messageHashFileName = formTextFileNameWith("hashed_message", randomFilePostfix);
        String messageHash = shaService.getHashOf(messageContent);
        String encrypedMessageHash = aesService.encrypt(messageHash, aesKey);
        writeToFileWithName(encrypedMessageHash, messageHashFileName);
        currentMessage.setHashedMessageFileName(messageHashFileName);
    }

    private String formTextFileNameWith(String name, int number) {
        return name + "_" + number + ".txt";
    }

    private void writeToFileWithName(String content, String fileName) throws IOException {
        Path encryptedAesKeyFilePath = fileService.getFilePathOf(fileName);
        fileService.writeToFile(content, encryptedAesKeyFilePath);
    }

    private void writeToFileWithName(byte[] content, String fileName) throws IOException {
        Path encryptedAesKeyFilePath = fileService.getFilePathOf(fileName);
        fileService.writeToFile(content, encryptedAesKeyFilePath);
    }

    @Override
    public void decryptDiffieHellman(Message message) throws IOException, NoSuchAlgorithmException {
        Key receiverPrivateKey = rsaService.getDecodedKey(message.getReceiver().getPrivateKey(), PrivateKey.class);
        SecretKey aesKey = recoverAesKey(message, receiverPrivateKey);
        String decryptedMessage = decryptMessageContent(message, aesKey);
        String actualMessageHash = shaService.getHashOf(decryptedMessage);
        String receivedMessageHash = recoverDecryptedMessageHash(message, aesKey);
        boolean messageWasAltered = actualMessageHash.equals(receivedMessageHash);

        if (messageWasAltered) {
            decryptedMessage += "MESSAGE WAS ALTERED";
        }
        message.setContent(decryptedMessage);
    }

    private SecretKey recoverAesKey(Message message, Key receiverPrivateKey) throws IOException {
        Path encryptedAesKeyPath = fileService.getFilePathOf(message.getEncryptedAesKeyFileName());
        byte[] encryptedAesKey = fileService.readFileContentAsBytes(encryptedAesKeyPath);
        byte[] decryptedAesKeyString = rsaService.decrypt(encryptedAesKey, receiverPrivateKey);
        return (SecretKey) aesService.getDecodedKey(decryptedAesKeyString);
    }

    private String decryptMessageContent(Message message, SecretKey aesKey) throws IOException {
        Path encryptedMessagePath = fileService.getFilePathOf(message.getEncryptedMessageFileName());
        String encryptedMessageContent = fileService.readFileContents(encryptedMessagePath);
        return aesService.decrypt(encryptedMessageContent, aesKey);
    }

    private String recoverDecryptedMessageHash(Message message, SecretKey aesKey) throws IOException {
        Path messageHashPath = fileService.getFilePathOf(message.getHashedMessageFileName());
        byte[] messageHashEncrypted = fileService.readFileContentAsBytes(messageHashPath);
        String messageHashEncryptedEncoded = Base64.getEncoder().encodeToString(messageHashEncrypted);
        return aesService.decrypt(messageHashEncryptedEncoded, aesKey);
    }
}
