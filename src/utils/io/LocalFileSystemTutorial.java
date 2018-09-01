package utils.io;

import org.junit.Assert;
import org.junit.Test;
import utils.common.MD5;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class LocalFileSystemTutorial {
    public static void main(String[] args) {
        LocalFileSystemTutorial tutorial = new LocalFileSystemTutorial();
        tutorial.testStreamRead();
        tutorial.testObjectIO();
        tutorial.testReaderRead();
    }
    public void testStreamRead(){
        LocalFileSystem fileSystem = LocalFileSystem.getFileSystem();
        String path = "E:\\Learn_HTML\\RangeRoverLaunch.mp4";
        long d = new Date().getTime();
        byte[] buf0 = fileSystem.streamRead(path);
        long d1 = new Date().getTime();
        byte[] buf = fileSystem.bufferedStreamRead(path);
        long d2 = new Date().getTime();
        Assert.assertArrayEquals(buf0, buf);
        System.out.println(d1 - d);
        System.out.println(d2 - d1);
    }

    public void testReaderRead(){
        LocalFileSystem fileSystem = LocalFileSystem.getFileSystem();
        String path = "E:\\Learn_HTML\\RangeRoverLaunch.mp4";
        String path2 = "E:\\Java_Workspace\\Java_Workspace.iml";
        boolean finished = fileSystem.merge(Arrays.asList(path, path2), "D:\\a.txt");
        byte[] buf = fileSystem.bufferedStreamRead("D:\\a.txt");
        if (buf != null) System.out.println(new String(buf));
        System.out.println(finished);
    }

    public void testObjectIO (){
        LocalFileSystem fileSystem = LocalFileSystem.getFileSystem();
        boolean finished = fileSystem.writeObject(new U(1, "a"), "D:\\a.doc");
        U u = (U) fileSystem.readObject("D:\\a.doc");
        System.out.println(u);
        System.out.println(finished);
    }
}
class U implements Serializable {
    private final static long serialVersionUID = 1L;
    int a;
    String b;
    U(int a, String b){
        this.a = a;
        this.b = b;
    }
    public String toString(){
        return this.a + ", " + this.b;
    }
}