package org.raidsphere;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class RSConfig {
    public String[] raidDisks;
    public String[] raidParityDisks;
    public String VirtualDiskMountPath;
    public String VirtualBlockSize;

    public RSConfig() {
        Properties prop = new Properties();
        String fileName = "src/main/resources/raid-sphere.conf";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        raidDisks = prop.getProperty("raid.disks").split(",");
        raidParityDisks = prop.getProperty("raid.parity.disks").split(",");

        if (raidDisks.length < 1) {
            throw new RuntimeException("raid.disks must have at least 1 disk");
        }

        if (raidParityDisks.length < 1) {
            throw new RuntimeException("raid.parity.disks must have at least 1 disk");
        }

        if (raidDisks.length != raidParityDisks.length) {
            throw new RuntimeException("raid.disks and raid.parity.disks must have the same number of disks");
        }

        for (int i = 0; i < raidDisks.length; i++) {
            if (raidDisks[i].equals(raidParityDisks[i])) {
                throw new RuntimeException("raid.disks and raid.parity.disks must not have the same disk");
            }
        }

        for (int i = 0; i < raidDisks.length; i++) {
            try {
                FileStore fileStore = Files.getFileStore(Paths.get(raidDisks[i]));
                FileStore parityFileStore = Files.getFileStore(Paths.get(raidParityDisks[i]));

                long blockSize = fileStore.getBlockSize();
                long parityBlockSize = parityFileStore.getBlockSize();

                if (blockSize != parityBlockSize) {
                    throw new RuntimeException("raid.disk and raid.parity.disk pairs must have the same block size");
                }
            }catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }

        }


        VirtualDiskMountPath = prop.getProperty("virtual_disk.mount.path");
        VirtualBlockSize = prop.getProperty("virtual_disk.block.size");
    }
}
