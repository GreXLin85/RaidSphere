package org.raidsphere;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RSConfig {
    public String raidDisks;
    public String raidParityDisk;
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

        raidDisks = prop.getProperty("raid.disks");
        raidParityDisk = prop.getProperty("raid.parity.disk");
        VirtualDiskMountPath = prop.getProperty("virtual_disk.mount.path");
        VirtualBlockSize = prop.getProperty("virtual_disk.block.size");
    }
}
