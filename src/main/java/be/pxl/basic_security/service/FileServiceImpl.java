package be.pxl.basic_security.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String readFileContents(Path filePath) throws IOException {
        return new String(Files.readAllBytes(filePath));
    }

    @Override
    public byte[] readFileContentAsBytes(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }

    @Override
    public void writeToFile(String message, Path filePath) throws IOException {
        Files.write(filePath, message.getBytes());
    }

    @Override
    public void writeToFile(byte[] content, Path filePath) throws IOException {
        Files.write(filePath, content);
    }

    private Path getMessagesFolder() {
        return Paths.get("src","main", "resources", "messages");
    }

    @Override
    public Path getFilePathOf(String messageFileName) {
        return getMessagesFolder().resolve(messageFileName);
    }
}
