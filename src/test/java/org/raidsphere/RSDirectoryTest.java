package org.raidsphere;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class RSDirectoryTest {
    RSDirectory directory;
    RSHash hash;

    @BeforeEach
    void setUp() {
        directory = new RSDirectory();
        try {
            hash = new RSHash();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        // Add a file to the root directory
        RSFile file = new RSFile("Hello, World!".getBytes(), hash.getHash("Hello, World!".getBytes()));
        directory.addFile("test.txt", file);

        // Add a directory to the root directory
        RSDirectory directory2 = new RSDirectory();
        RSFile file2 = new RSFile("Hello, World!".getBytes(), hash.getHash("Hello, World!".getBytes()));

        directory
                .addDirectory("test", directory2)
                // and add a file to the directory we just added
                .addFile("test.txt", file2);
    }

    @Test
    void getFiles() {
        assertEquals(directory.getFiles().size(), 1);
    }

    @Test
    void getDirectories() {
        assertEquals(directory.getDirectories().size(), 1);
    }

    @Test
    void getFile() {
        directory.addFile("test3.txt", new RSFile("Hello, World!".getBytes(), hash.getHash("Hello, World!".getBytes())));
        assertEquals(directory.getFile("test3.txt").getFullChecksum(), hash.getHash("Hello, World!".getBytes()));
    }

    @Test
    void getDirectory() {
        assertEquals(directory.getDirectory("test").getFiles().size(), 1);
    }

    @Test
    void addFile() {
        directory.addFile("test2.txt", new RSFile("Hello, World!".getBytes(), hash.getHash("Hello, World!".getBytes())));
        assertEquals(directory.getFiles().size(), 2);
    }

    @Test
    void addDirectory() {
        directory.addDirectory("test2", new RSDirectory());
        assertEquals(directory.getDirectories().size(), 2);
    }

    @Test
    void removeFile() {
        directory.removeFile("test.txt");
        assertEquals(directory.getFiles().size(), 0);
    }

    @Test
    void removeDirectory() {
        directory.removeDirectory("test");
        assertEquals(directory.getDirectories().size(), 0);
    }

    @Test
    void hasFile() {
        directory.addFile("test5.txt", new RSFile("Hello, World!".getBytes(), hash.getHash("Hello, World!".getBytes())));
        assertTrue(directory.hasFile("test5.txt"));
    }

    @Test
    void hasDirectory() {
        directory.addDirectory("test2", new RSDirectory());
        assertTrue(directory.hasDirectory("test2"));
    }

    @Test
    void isEmpty() {
        assertFalse(directory.isEmpty());
    }

    @Test
    void isNotEmpty() {
        assertTrue(directory.isNotEmpty());
    }

    @Test
    void isFile() {
        directory.addFile("test2.txt", new RSFile("Hello, World!".getBytes(), hash.getHash("Hello, World!".getBytes())));
        assertTrue(directory.isFile("test2.txt"));
    }

    @Test
    void isDirectory() {
        directory.addDirectory("test3", new RSDirectory());

        assertTrue(directory.isDirectory("test3"));
    }
}