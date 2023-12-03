package org.raidsphere;

public class RSStatistics {
    private long totalSize = 0;
    private long totalDataBlocks = 0;
    private long freeSize = 0;
    private long freeDataBlocks = 0;
    private long usedSize = 0;
    private long usedDataBlocks = 0;

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getTotalDataBlocks() {
        return totalDataBlocks;
    }

    public void setTotalDataBlocks(long totalDataBlocks) {
        this.totalDataBlocks = totalDataBlocks;
    }

    public long getFreeSize() {
        return freeSize;
    }

    public void setFreeSize(long freeSize) {
        this.freeSize = freeSize;
    }

    public long getFreeDataBlocks() {
        return freeDataBlocks;
    }

    public void setFreeDataBlocks(long freeDataBlocks) {
        this.freeDataBlocks = freeDataBlocks;
    }

    public long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(long usedSize) {
        this.usedSize = usedSize;
    }

    public long getUsedDataBlocks() {
        return usedDataBlocks;
    }

    public void setUsedDataBlocks(long usedDataBlocks) {
        this.usedDataBlocks = usedDataBlocks;
    }
}
