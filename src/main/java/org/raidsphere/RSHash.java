package org.raidsphere;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RSHash {
    MessageDigest md = MessageDigest.getInstance("MD5");

    public RSHash() throws NoSuchAlgorithmException {
    }

    /**
     * Gets the MD5 hash of the given file.
     *
     * @param file The file to get the hash of.
     */
    public String getHash(byte[] file) {
        byte[] array = md.digest(file);
        StringBuffer sb = new StringBuffer();
        for (byte b : array) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString();
    }
}
