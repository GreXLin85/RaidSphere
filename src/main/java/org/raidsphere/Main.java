package org.raidsphere;

import java.nio.file.Paths;

import jnr.ffi.Platform;

public class Main {
    public static void main(String[] args) {
        RaidSphereFS fs = new RaidSphereFS();

        try {
            String path;
            switch (Platform.getNativePlatform().getOS()) {
                case WINDOWS:
                    path = "J:\\";
                    break;
                default:
                    path = "/tmp/mnth";
            }

            fs.mount(Paths.get(path), true, true);
            System.out.println("Mounted!");
        } finally {
            fs.umount();
            System.out.println("Unmounted!");
        }
    }
}