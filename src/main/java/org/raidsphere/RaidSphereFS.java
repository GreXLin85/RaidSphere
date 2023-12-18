package org.raidsphere;

import jnr.ffi.Platform;
import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import ru.serce.jnrfuse.struct.Statvfs;

import java.util.Collections;
import java.util.List;

import static jnr.ffi.Platform.OS.WINDOWS;

public class RaidSphereFS extends FuseStubFS {
    private RSProxy proxy = new RSProxy();
    public RaidSphereFS() {
        // load some virtual test files
        proxy.write("/test1.txt", "Hello World!".getBytes(), 12, 0);
        proxy.write("/test2.txt", "Hello World!".getBytes(), 12, 0);
        proxy.mkdir("/testdir");
    }

    @Override
    public int getattr(String path, FileStat stat) {
        if (path.equals("/")) {
            stat.st_mode.set(FileStat.S_IFDIR | 0755);
            stat.st_nlink.set(2);
        } else if (proxy.hasFile(path)) {
            stat.st_mode.set(FileStat.S_IFREG | 0444);
            stat.st_nlink.set(1);
            stat.st_size.set(proxy.getFile(path).getSize());
        } else if (proxy.hasDirectory(path)) {
            stat.st_mode.set(FileStat.S_IFDIR | 0755);
            stat.st_nlink.set(2);
        } else {
            return -ErrorCodes.ENOENT();
        }

        stat.st_uid.set(getContext().uid.get());
        stat.st_gid.set(getContext().gid.get());

        return 0;
    }

    @Override
    public int write(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        System.out.println(buf);

        return 0;
        //return proxy.write(path, buf.array(), size, offset, fi);
    }

    @Override
    public int read(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        return proxy.read(path, buf, size, offset, fi);
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        return 0;
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filler, @off_t long offset, FuseFileInfo fi) {
        filler.apply(buf, ".", null, 0);
        filler.apply(buf, "..", null, 0);

        List<RSDirectory> directories = proxy.getDirectories(path);

        for (RSDirectory directory : directories) {
            filler.apply(buf, directory.name, null, 0);
        }

        List<RSFile> files = proxy.getFiles(path);

        for (RSFile file : files.keySet()) {
            filler.apply(buf, file.name, null, 0);
        }

        return 0;
    }

    @Override
    public int statfs(String path, Statvfs stbuf) {
        if (Platform.getNativePlatform().getOS() == WINDOWS) {
            if ("/".equals(path)) {
                stbuf.f_blocks.set(proxy.statistics.getTotalDataBlocks()); // total data blocks in file system
                stbuf.f_frsize.set(proxy.BLOCK_SIZE);        // fs block size
                stbuf.f_bfree.set(proxy.statistics.getFreeDataBlocks());  // free blocks in fs
            }
        }
        return super.statfs(path, stbuf);
    }

    @Override
    public int mkdir(String path, long mode) {
        if (!proxy.hasDirectory(path)) {
            return proxy.mkdir(path);
        } else {
            System.out.println("Directory already exists: " + path);
            return -ErrorCodes.EEXIST();
        }
    }

    /*@Override
    public int rename(String oldpath, String newpath) {
        return proxy.rename(oldpath, newpath);
    }*/
}
