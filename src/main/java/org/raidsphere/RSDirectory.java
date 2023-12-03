package org.raidsphere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RSDirectory {
    public String name;
    private HashMap<String, RSFile> files = new HashMap<String, RSFile>();
    private HashMap<String, RSDirectory> directories = new HashMap<String, RSDirectory>();

    public RSDirectory(String name) {
        this.name = name;
    }

    public RSDirectory() {
        this.name = "ROOT";
    }

    public List<String> getFiles() {
        return new ArrayList<String>(files.keySet());
    }

    public List<String> getDirectories() {
        return new ArrayList<String>(directories.keySet());
    }

    public RSFile getFile(String name) {
        return files.get(name);
    }

    public RSDirectory getDirectory(String name) {
        return directories.get(name);
    }

    public void addFile(String name, RSFile file) {
        files.put(name, file);
    }

    public RSDirectory addDirectory(String name, RSDirectory directory) {
        directories.put(name, directory);

        return directory;
    }

    public void removeFile(String name) {
        files.remove(name);
    }

    public void removeDirectory(String name) {
        directories.remove(name);
    }

    public boolean hasFile(String name) {
        return files.containsKey(name);
    }

    public boolean hasDirectory(String name) {
        return directories.containsKey(name);
    }

    public boolean isEmpty() {
        return files.isEmpty() && directories.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean isFile(String name) {
        return hasFile(name);
    }

    public boolean isDirectory(String name) {
        return hasDirectory(name);
    }
}
