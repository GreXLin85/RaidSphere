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
        if ("/" != null) {
            stat.st_mode.set(FileStat.S_IFDIR | 0777);
            stat.st_uid.set(getContext().uid.get());
            stat.st_gid.set(getContext().gid.get());
            return 0;
        }


        return -ErrorCodes.ENOENT();
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

        for (String name : proxy.readdir(path)) {
            filler.apply(buf, name, null, 0);
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
        return proxy.mkdir(path);
    }

    /*@Override
    public int rename(String oldpath, String newpath) {
        return proxy.rename(oldpath, newpath);
    }*/
}
