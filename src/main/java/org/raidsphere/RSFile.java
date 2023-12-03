package org.raidsphere;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

public class RSFile {
    private ByteBuffer contents = ByteBuffer.allocate(0);
    private String fullChecksum;
    private String parityChecksum;


    /**
     * Creates a new file with the given contents.
     *
     * @param contents The contents of the file.
     */
    public RSFile(byte[] contents, String fullChecksum) {
        this.contents = ByteBuffer.wrap(contents);
        this.fullChecksum = fullChecksum;

        try {
            RSHash rsHash = new RSHash();
            this.parityChecksum = rsHash.getHash(this.contents.array());
        } catch (NoSuchAlgorithmException e) {
            // Not possible
        }
    }

    /**
     * Creates a new empty file.
     */
    public RSFile() {
    }

    /**
     * Gets the contents of the file.
     *
     * @return The contents of the file.
     */
    public byte[] getContents() {
        return contents.array();
    }

    /**
     * Sets the contents of the file to the given contents.
     *
     * @param contents The contents to set.
     */
    public void setContents(byte[] contents) {
        this.contents = ByteBuffer.wrap(contents);

    }

    /**
     * Gets the full checksum of the file.
     *
     * @return The full checksum of the file.
     */
    public String getFullChecksum() {
        return fullChecksum;
    }

    /**
     * Gets the parity checksum of the file.
     *
     * @return The parity checksum of the file.
     */
    public String getParityChecksum() {
        return parityChecksum;
    }

    /**
     * Appends the given contents to the end of the file.
     *
     * @param contents The contents to append.
     */
    public void appendContents(byte[] contents) {
        ByteBuffer newContents = ByteBuffer.allocate(this.contents.capacity() + contents.length);
        newContents.put(this.contents);
        newContents.put(contents);
        this.contents = newContents;
    }

    /**
     * Removes the contents of the file from start to end.
     *
     * @param start The start index of the removal.
     * @param end   The end index of the removal.
     */
    public void removeContents(int start, int end) {
        ByteBuffer newContents = ByteBuffer.allocate(this.contents.capacity() - (end - start));
        newContents.put(this.contents.array(), 0, start);
        newContents.put(this.contents.array(), end, this.contents.capacity() - end);
        this.contents = newContents;
    }

    /**
     * Inserts the given contents at the given start index.
     *
     * @param start    The start index of the insertion.
     * @param contents The contents to insert.
     */
    public void insertContents(int start, byte[] contents) {
        ByteBuffer newContents = ByteBuffer.allocate(this.contents.capacity() + contents.length);
        newContents.put(this.contents.array(), 0, start);
        newContents.put(contents);
        newContents.put(this.contents.array(), start, this.contents.capacity() - start);
        this.contents = newContents;
    }

    /**
     * Replaces the contents of the file from start to end with the given contents.
     *
     * @param start    The start index of the replacement.
     * @param end      The end index of the replacement.
     * @param contents The contents to replace with.
     */
    public void replaceContents(int start, int end, byte[] contents) {
        ByteBuffer newContents = ByteBuffer.allocate(this.contents.capacity() - (end - start) + contents.length);
        newContents.put(this.contents.array(), 0, start);
        newContents.put(contents);
        newContents.put(this.contents.array(), end, this.contents.capacity() - end);
        this.contents = newContents;
    }

    /**
     * Gets the size of the file.
     *
     * @return The size of the file.
     */
    public int getSize() {
        return contents.capacity();
    }

    /**
     * Checks if the file is empty.
     *
     * @return Whether the file is empty.
     */
    public boolean isEmpty() {
        return contents.capacity() == 0;
    }
}
