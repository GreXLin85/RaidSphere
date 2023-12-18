package org.raidsphere;

public class RSPath {
    /**
     * fullPath includes VirtualDiskMountPath to the given path in constructor
     */
    private String fullPath;
    /**
     * path is the path information without any file information
     */
    private String path;
    /**
     * fileName is just file's name in the path without any extension or part information
     */
    private String fileName;
    /**
     * fileExtension is just file extension in the path without any name or part information
     */
    private String fileExtension;
    /**
     * partNumber is just parity part number
     */
    private long partNumber;

    public RSPath(String path) {
        RSConfig config = new RSConfig();
        this.fullPath = config.VirtualDiskMountPath + path;
        this.path = path;
        if (isPathIncludesFileInformation()) {
            this.path = getPathWithoutFileInformation();
            this.fileName = getFileNameWithoutExtension();
            this.fileExtension = getFileExtension();
            if (isPathIncludesPartInformation()) {
                this.partNumber = getFilePartNumber();
            }else {
                this.partNumber = 0;
            }
        } else {
            this.fileName = null;
            this.partNumber = 0;
        }
    }

    private String getPathWithoutFileInformation() {
        int end = path.contains(".part") ? path.indexOf(".part") : path.lastIndexOf("/");
        return path.substring(0, end);
    }

    private String getFileNameWithoutExtension() {
        int start = path.lastIndexOf("/") + 1;
        int end = path.contains(".part") ? path.indexOf(".part") : path.lastIndexOf(".");
        return path.substring(start, end);
    }

    private String getFileExtension() {
        int start = path.lastIndexOf(".") + 1;
        int end = path.contains(".part") ? path.indexOf(".part") : path.length();
        return path.substring(start, end);
    }

    private long getFilePartNumber() {
        int start = path.lastIndexOf("part") + 4;
        return Long.parseLong(path.substring(start));
    }

    public boolean isPathIncludesFileInformation() {
        return path.contains(".");
    }

    public boolean isPathIncludesPartInformation() {
        return path.contains(".part");
    }

    public boolean isDirectory() {
        return path.endsWith("/");
    }

    public boolean isFile() {
        return !isDirectory();
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getPath() {
        return path;
    }

    public String getName(boolean withExtension, boolean withPartInformation) {
        String tempName = fileName;
        if (withExtension) {
            tempName = tempName.concat(".").concat(fileExtension);
        }
        if (withPartInformation) {
            tempName = tempName.concat(".part").concat(String.valueOf(partNumber));
        }

        return String.valueOf(tempName);
    }

    public String getExtension() {
        return fileExtension;
    }

    public long getPartNumber() {
        return partNumber;
    }


}
