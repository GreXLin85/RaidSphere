package org.raidsphere;

import java.nio.file.Paths;

import jnr.ffi.Platform;

public class Main {
    public static void main(String[] args) {
        RaidSphereFS fs = new RaidSphereFS();
        RSConfig config = new RSConfig();

        try {
            String path = config.VirtualDiskMountPath;
            System.out.println("Mounting to " + path);

            fs.mount(Paths.get(path), true, true);
        } finally {
            fs.umount();
            System.out.println("Unmounted!");
        }
    }
}