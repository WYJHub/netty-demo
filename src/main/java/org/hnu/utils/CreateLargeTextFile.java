package org.hnu.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreateLargeTextFile {
    public static void main(String[] args) {
        String fileName = "large_file.txt";
        long fileSize = 2L * 1024 * 1024 * 1024; // 2GB

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            long bytesWritten = 0;
            while (bytesWritten < fileSize) {
                writer.write(generateRandomString());
                bytesWritten += 1024; // Writing 1KB chunks
            }
            writer.close();
            System.out.println("File created successfully!");
        } catch (IOException e) {
            System.out.println("Error creating the file: " + e.getMessage());
        }
    }

    private static String generateRandomString() {
        // Generating random string (you can modify this method if needed)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1024; i++) {
            sb.append((char) ((int) (Math.random() * 26) + 'a'));
        }
        return sb.toString();
    }
}
