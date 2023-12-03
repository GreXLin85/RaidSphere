package org.raidsphere;

import jnr.ffi.Pointer;
import ru.serce.jnrfuse.struct.FuseFileInfo;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RSProxy {
    public RSStatistics statistics = new RSStatistics();
    private RSConfig config = new RSConfig();
    public final int BLOCK_SIZE = config.VirtualBlockSize; // Block size in bytes
    private HashMap<String, RSDisk> dataDisks = new HashMap<String, RSDisk>();
    private HashMap<String, RSDisk> parityDisks = new HashMap<String, RSDisk>();

    public RSProxy() {
        // load raid disks
        for (int i = 0; i < config.raidDisks.length; i++) {
            String raidDiskPath = config.raidDisks[i];
            String parityDiskPath = config.raidParityDisks[i];

            FileStore fileStore = null;
            FileStore parityFileStore = null;
            long diskBlockSize;
            long parityBlockSize;
            try {
                fileStore = Files.getFileStore(Paths.get(raidDiskPath));
                parityFileStore = Files.getFileStore(Paths.get(parityDiskPath));

                diskBlockSize = fileStore.getBlockSize();
                long diskTotalSize = fileStore.getTotalSpace();
                long diskFreeSize = fileStore.getUsableSpace();
                parityBlockSize = parityFileStore.getBlockSize();
                long parityTotalSize = parityFileStore.getTotalSpace();
                long parityFreeSize = parityFileStore.getUsableSpace();

                long totalBlocks = (diskTotalSize / BLOCK_SIZE);
                long usedBlocks = ((diskTotalSize - diskFreeSize) / BLOCK_SIZE);
                long freeBlocks = totalBlocks - usedBlocks;

                statistics.setTotalSize(statistics.getTotalSize() + diskTotalSize);
                statistics.setFreeSize(statistics.getFreeSize() + diskFreeSize);
                statistics.setUsedSize(statistics.getUsedSize() + (diskTotalSize - diskFreeSize));
                statistics.setTotalDataBlocks(statistics.getTotalDataBlocks() + totalBlocks);
                statistics.setFreeDataBlocks(statistics.getFreeDataBlocks() + freeBlocks);
                statistics.setUsedDataBlocks(statistics.getUsedDataBlocks() + usedBlocks);

                dataDisks.put(raidDiskPath, new RSDisk((int) diskBlockSize, diskTotalSize, diskFreeSize));
                parityDisks.put(parityDiskPath, new RSDisk((int) parityBlockSize, parityTotalSize, parityFreeSize));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int read(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        byte[] fullFile = new byte[0];
        int fullFileSize = 0;

        for (int i = 0; i < config.raidDisks.length; i++) {
            String raidDiskPath = config.raidDisks[i];
            RSDisk raidDisk = dataDisks.get(raidDiskPath);
            RSFile file = raidDisk.getFile(path + ".part" + i);

            if (file != null) {
                byte[] fileData = file.getContents();
                byte[] fileDataChunk = new byte[(int) size];
                System.arraycopy(fileData, (int) offset, fileDataChunk, 0, (int) size);
                fullFile = fileDataChunk;
                fullFileSize = fileDataChunk.length;
            }
        }

        buf.put(0, fullFile, 0, fullFileSize);
        return fullFileSize;
    }

    public List<byte[]> getFileChunks(byte[] mainFile) {
        // split file by length of disks
        int chunkSize = mainFile.length / config.raidDisks.length;
        List<byte[]> fileChunks = new ArrayList<byte[]>();
        for (int i = 0; i < config.raidDisks.length; i++) {
            byte[] fileChunk = new byte[chunkSize];
            System.arraycopy(mainFile, i * chunkSize, fileChunk, 0, chunkSize);
            fileChunks.add(fileChunk);
        }

        return fileChunks;
    }


    public int write(String path, byte[] buffer, long bufSize, long writeOffset) {
        List<byte[]> fileChunks = getFileChunks(buffer);

        for (int i = 0; i < config.raidDisks.length; i++) {
            String raidDiskPath = config.raidDisks[i];
            RSDisk raidDisk = dataDisks.get(raidDiskPath);
            RSFile file = raidDisk.getFile(path + ".part" + i);

            if (file == null) {
                try {
                    RSHash hash = new RSHash();
                    file = new RSFile(buffer, hash.getHash(buffer));
                    raidDisk.addFile(path + ".part" + i, file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                file.setContents(fileChunks.get(i));
            }
        }

        return 0;
    }

    public int mkdir(String path) {
        for (int i = 0; i < config.raidDisks.length; i++) {
            String raidDiskPath = config.raidDisks[i];
            RSDisk raidDisk = dataDisks.get(raidDiskPath);
            RSDirectory directory = new RSDirectory(path);
            raidDisk.addDirectory(path, directory);
        }

        return 0;
    }

    public List<String> readdir(String path) {
        List<String> files = new ArrayList<String>();

        for (int i = 0; i < config.raidDisks.length; i++) {
            String raidDiskPath = config.raidDisks[i];
            RSDisk raidDisk = dataDisks.get(raidDiskPath);
            files.addAll(raidDisk.getDirectory(path).getFiles());
        }
        return files;
    }
}
