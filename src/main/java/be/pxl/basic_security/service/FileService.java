package be.pxl.basic_security.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileService {
    String readFileContents(Path filePath) throws IOException;
    byte[] readFileContentAsBytes(Path filePath) throws IOException;
    void writeToFile(String content, Path filePath) throws IOException;
    void writeToFile(byte[] content, Path filePath) throws IOException;
    Path getFilePathOf(String messageFileName);
}
