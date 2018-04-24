package utils.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final public class HDFSFileSystem {
    private static volatile FileSystem fileSystem = null;
    private HDFSFileSystem (){}
    public static FileSystem getFileSystem() throws IOException {
        if (fileSystem == null) {
            synchronized(HDFSFileSystem.class) {
                if (fileSystem == null) {
                    Configuration conf = new Configuration();
                    conf.addResource(new Path("core-site.xml的路径"));
                    conf.addResource(new Path("hdfs-site.xml的路径"));
                    conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
                    conf.set("fs.file.imple", org.apache.hadoop.fs.LocalFileSystem.class.getName());
                    fileSystem = FileSystem.get(conf);
                }
            }
        }
        return fileSystem;
    }

    public static String readFile(String pathStr){
        try {
            FileSystem fileSystem = HDFSFileSystem.getFileSystem();
            Path path = new Path(pathStr);
            if (fileSystem.isFile(path)) {
                FSDataInputStream fsis = fileSystem.open(path);
                byte[] buf = new byte[1024];
                StringBuilder sb = new StringBuilder();
                int len;
                while ((len = fsis.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                fsis.close();
                return new String(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean writeFile(String pathStr, String content) {
        try {
            Path path = new Path(pathStr);
            FSDataOutputStream fsos = HDFSFileSystem.getFileSystem().create(path, true);  // true-> overwrite
            byte[] buf = content.getBytes();
            fsos.write(buf);
            fsos.flush();
            fsos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void selectFiles(String pathStr) {
        // 仅仅是写个大概，具体的条件函数和返回值需要根据实际需求来写
        List<Path> filePaths = new ArrayList<>();
        try {
            Path path = new Path(pathStr);
            FileSystem fileSystem = HDFSFileSystem.getFileSystem();
            if (fileSystem.exists(path)) {
                fileSystem.listStatus(path, subPath -> {
                    // subPath means path of files or directories under path
                    try {
                        if (fileSystem.isFile(subPath)) {
                            String subFileName = subPath.getName();
                            FileStatus fileStatus = fileSystem.getFileStatus(subPath);
                            long lastModifiedTime = fileStatus.getModificationTime();
                            if (subFileName.startsWith("aaa")) filePaths.add(subPath);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
