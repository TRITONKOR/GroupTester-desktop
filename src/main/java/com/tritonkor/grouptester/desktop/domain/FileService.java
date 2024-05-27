package com.tritonkor.grouptester.desktop.domain;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    public Path getPathFromResource(String resourceName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path tempFile = Files.createTempFile("temp", null);
        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourceName);
            }
            Files.copy(inputStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile;
    }

    public byte[] getBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}
