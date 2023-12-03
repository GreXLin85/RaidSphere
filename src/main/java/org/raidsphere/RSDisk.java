package org.raidsphere;

public class RSDisk {
    private RSDirectory root = new RSDirectory();
    private int blockSize;
    private long totalSize;
    private long freeSize;

    public RSDisk(int blockSize, long totalSize, long freeSize) {
        this.blockSize = blockSize;
        this.totalSize = totalSize;
        this.freeSize = freeSize;
    }

    public RSDirectory getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return root.isEmpty();
    }

    public boolean isNotEmpty() {
        return root.isNotEmpty();
    }

    public void addFile(String path, RSFile file) {
        String[] pathParts = path.split("/");
        RSDirectory currentDirectory = root;

        for (int i = 0; i < pathParts.length - 1; i++) {
            String pathPart = pathParts[i];

            if (!currentDirectory.hasDirectory(pathPart)) {
                currentDirectory.addDirectory(pathPart, new RSDirectory(pathPart));
            }

            currentDirectory = currentDirectory.getDirectory(pathPart);
        }

        currentDirectory.addFile(pathParts[pathParts.length - 1], file);
    }

    public void addDirectory(String path, RSDirectory directory) {
        String[] pathParts = path.split("/");
        RSDirectory currentDirectory = root;

        for (int i = 0; i < pathParts.length - 1; i++) {
            String pathPart = pathParts[i];

            if (!currentDirectory.hasDirectory(pathPart)) {
                currentDirectory.addDirectory(pathPart, new RSDirectory(pathPart));
            }

            currentDirectory = currentDirectory.getDirectory(pathPart);
        }

        currentDirectory.addDirectory(pathParts[pathParts.length - 1], directory);
    }

    public void removeFile(String path) {
        String[] pathParts = path.split("/");
        RSDirectory currentDirectory = root;

        for (int i = 0; i < pathParts.length - 1; i++) {
            String pathPart = pathParts[i];

            if (!currentDirectory.hasDirectory(pathPart)) {
                return;
            }

            currentDirectory = currentDirectory.getDirectory(pathPart);
        }

        currentDirectory.removeFile(pathParts[pathParts.length - 1]);
    }

    public void removeDirectory(String path) {
        String[] pathParts = path.split("/");
        RSDirectory currentDirectory = root;

        for (int i = 0; i < pathParts.length - 1; i++) {
            String pathPart = pathParts[i];

            if (!currentDirectory.hasDirectory(pathPart)) {
                return;
            }

            currentDirectory = currentDirectory.getDirectory(pathPart);
        }

        currentDirectory.removeDirectory(pathParts[pathParts.length - 1]);
    }

    public boolean hasFile(String path) {
        String[] pathParts = path.split("/");
        RSDirectory currentDirectory = root;

        for (int i = 0; i < pathParts.length - 1; i++) {
            String pathPart = pathParts[i];

            if (!currentDirectory.hasDirectory(pathPart)) {
                return false;
            }

            currentDirectory = currentDirectory.getDirectory(pathPart);
        }

        return currentDirectory.hasFile(pathParts[pathParts.length - 1]);
    }

    public boolean hasDirectory(String path) {
        String[] pathParts = path.split("/");
        RSDirectory currentDirectory = root;

        for (int i = 0; i < pathParts.length - 1; i++) {
            String pathPart = pathParts[i];

            if (!currentDirectory.hasDirectory(pathPart)) {
                return false;
            }

            currentDirectory = currentDirectory.getDirectory(pathPart);
        }

        return currentDirectory.hasDirectory(pathParts[pathParts.length - 1]);
    }

    public RSFile getFile(String path) {
        String[] pathParts = path.split("/");
        RSDirectory currentDirectory = root;

        for (int i = 0; i < pathParts.length - 1; i++) {
            String pathPart = pathParts[i];

            if (!currentDirectory.hasDirectory(pathPart)) {
                return null;
            }

            currentDirectory = currentDirectory.getDirectory(pathPart);
        }

        return currentDirectory.getFile(pathParts[pathParts.length - 1]);
    }

    public RSDirectory getDirectory(String path) {
        String[] pathParts = path.split("/");
        RSDirectory currentDirectory = root;

        for (int i = 0; i < pathParts.length - 1; i++) {
            String pathPart = pathParts[i];

            if (!currentDirectory.hasDirectory(pathPart)) {
                return null;
            }

            currentDirectory = currentDirectory.getDirectory(pathPart);
        }

        return currentDirectory.getDirectory(pathParts[pathParts.length - 1]);
    }

    public boolean isFile(String path) {
        return hasFile(path);
    }

    public boolean isDirectory(String path) {
        return hasDirectory(path);
    }

    public boolean isRoot(String path) {
        return path.equals("/");
    }

    public boolean isNotRoot(String path) {
        return !isRoot(path);
    }
}
