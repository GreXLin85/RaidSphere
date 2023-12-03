package org.raidsphere;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RSFileTest {
    private RSFile rsFile;
    private RSHash hash;

    @BeforeEach
    void setUp() {
        try {
            hash = new RSHash();
        } catch (Exception e) {
            e.printStackTrace();
        }
        rsFile = new RSFile("Hello World!".getBytes(), hash.getHash("Hello World!".getBytes()));
    }

    @Test
    void getContents() {
        assertEquals("Hello World!", new String(rsFile.getContents()));
    }

    @Test
    void getFullChecksum() {
        assertEquals(hash.getHash("Hello World!".getBytes()), rsFile.getFullChecksum());
    }

    @Test
    void getParityChecksum() {
        assertEquals(hash.getHash("Hello World!".getBytes()), rsFile.getParityChecksum());
    }

    @Test
    void getSize() {
        assertEquals("Hello World!".length(), rsFile.getSize());
    }

    @Test
    void isEmpty() {
        assertFalse(rsFile.isEmpty());
    }
}