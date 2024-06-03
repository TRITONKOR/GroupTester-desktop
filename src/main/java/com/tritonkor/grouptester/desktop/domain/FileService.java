package com.tritonkor.grouptester.desktop.domain;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

/**
 * Service class for file operations in the GroupTester desktop application.
 */
@Service
public class FileService {

    /**
     * Retrieves the path of a resource from the classpath.
     *
     * @param resourceName The name of the resource.
     * @return The path of the resource.
     * @throws IOException if an I/O error occurs.
     * @throws IllegalArgumentException if the specified resource is not found.
     */
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

    /**
     * Reads the contents of a file into a byte array.
     *
     * @param path The path of the file.
     * @return The byte array containing the file contents.
     * @throws IOException if an I/O error occurs.
     */
    public byte[] getBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}
