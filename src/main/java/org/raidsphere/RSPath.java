package org.raidsphere;

public class RSPath {
    private final String path;

    public RSPath(String path) {
        RSConfig config = new RSConfig();
        this.path = config.VirtualDiskMountPath + path;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        String[] pathParts = path.split("/");
        return pathParts[pathParts.length - 1];
    }
}
