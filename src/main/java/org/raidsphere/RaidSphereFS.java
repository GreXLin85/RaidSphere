package org.raidsphere;

import jnr.ffi.Platform;
import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseException;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.Statvfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jnr.ffi.Platform.OS.WINDOWS;

public class RaidSphereFS extends FuseStubFS {
    private static final int BLOCK_SIZE = 4096; // Block size in bytes
    private static final int NUM_DISKS = 4;
    private List<byte[]> dataDisks;
    private byte[] parityDisk;

    public RaidSphereFS() {
        dataDisks = new ArrayList<>();
        for (int i = 0; i < NUM_DISKS - 1; i++) {
            dataDisks.add(new byte[BLOCK_SIZE]);
        }
        parityDisk = new byte[BLOCK_SIZE];
    }

    @Override
    public int getattr(String path, FileStat stat) {
        if ("/" != null) {
            stat.st_mode.set(FileStat.S_IFDIR | 0777);
            stat.st_uid.set(getContext().uid.get());
            stat.st_gid.set(getContext().gid.get());
            return 0;
        }
        return -ErrorCodes.ENOENT();
    }

    @Override
    public int read(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        if (!"/hello".equals(path)) {
            return -ErrorCodes.ENOENT();
        }

        byte[] bytes = "HELLO_STR".getBytes();
        int length = bytes.length;
        if (offset < length) {
            if (offset + size > length) {
                size = length - offset;
            }
            buf.put(0, bytes, 0, bytes.length);
        } else {
            size = 0;
        }
        return (int) size;
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        if (!"/hello".equals(path)) {
            return -ErrorCodes.ENOENT();
        }
        return 0;
    }


    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, @off_t long offset, FuseFileInfo fi) {
        if (!"/".equals(path)) {
            return -ErrorCodes.ENOENT();
        }

        filter.apply(buf, ".", null, 0);
        filter.apply(buf, "..", null, 0);
        filter.apply(buf, "/", null, 0);
        filter.apply(buf, "/hello", null, 0);
        return 0;
    }

    @Override
    public int statfs(String path, Statvfs stbuf) {
        if (Platform.getNativePlatform().getOS() == WINDOWS) {
            if ("/".equals(path)) {
                stbuf.f_blocks.set(1024 * 1024); // total data blocks in file system
                stbuf.f_frsize.set(1024);        // fs block size
                stbuf.f_bfree.set(1024 * 1024);  // free blocks in fs
            }
        }
        return super.statfs(path, stbuf);
    }
}
