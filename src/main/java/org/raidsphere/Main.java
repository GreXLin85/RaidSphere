package org.raidsphere;

import jnr.ffi.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        RaidSphereFS fs = new RaidSphereFS();
        RSConfig config = new RSConfig();

        try {
            String path = config.VirtualDiskMountPath;
            System.out.println("Mounting to " + path);

            if (Platform.getNativePlatform().getOS() == Platform.OS.LINUX) {
                path = String.valueOf(Files.createTempDirectory(path.split("/")[path.split("/").length - 1]));
            }

            fs.mount(Paths.get(path), true, true);
        } finally {
            fs.umount();
            System.out.println("Unmounted!");
        }
    }
}