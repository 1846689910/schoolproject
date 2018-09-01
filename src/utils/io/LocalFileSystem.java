package utils.io;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.function.Function;

/**
 * used to read, write files in multiple ways and write object and properties files
 * */
final public class LocalFileSystem {
    private static volatile LocalFileSystem fileSystem = null;
    private LocalFileSystem(){}
    public static LocalFileSystem getFileSystem(){
        if (fileSystem == null) {
            synchronized (LocalFileSystem.class) {
                if (fileSystem == null) {
                    fileSystem = new LocalFileSystem();
                }
            }
        }
        return fileSystem;
    }

    /** 输入字节流
     * @param path: 读取文件的地址
     * @return 返回文件内容的字符串形式，如果读取出错，则返回null
     * */
    public byte[] streamRead(String path){
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  // 如果是可读内容可以使用StringBuilder sb = new StringBuilder()
            int len;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);  // 可读内容可以用sb.append(new String(buf, 0, len))
            }
            baos.flush();
            baos.close();
            fis.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /** 缓冲输入字节流
     * 推荐使用，效率更高
     * */
    public byte[] bufferedStreamRead(String path){
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            baos.close();
            bis.close();
            fis.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /** 输出字节流 */
    public boolean streamWrite(String path, byte[] buf) {
        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buf);
            fos.close();
            return true;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /** 缓冲输出字节流(推荐) */
    public boolean bufferedStreamWrite(String path, byte[] buf){
        File file = new File(path);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(buf);
            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 输入字符流
     * */
    public char[] readerRead(String path){
        File file = new File(path);
        try {
            FileReader fr = new FileReader(file);
            CharArrayWriter caw = new CharArrayWriter();  // 如果是可读内容可以使用StringBuilder sb = new StringBuilder()
            int len;
            char[] buf = new char[1024];
            while ((len = fr.read(buf)) != -1) {
                caw.write(buf, 0, len);  // 可读内容可以用sb.append(new String(buf, 0, len))
            }
            caw.flush();
            fr.close();
            return caw.toCharArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 缓冲输入字符流(推荐)
     * */
    public char[] bufferedReaderRead(String path){
        File file = new File(path);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            CharArrayWriter caw = new CharArrayWriter();  // 如果是可读内容可以使用StringBuilder sb = new StringBuilder()
            int len;
            char[] buf = new char[1024];
            while ((len = br.read(buf)) != -1) {
                caw.write(buf, 0, len);  // 可读内容可以用sb.append(new String(buf, 0, len))
            }
            caw.flush();
            br.close();
            fr.close();
            return caw.toCharArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /** 输入输出字符流 */
    public boolean Writerwrite(String path, char[] buf){
        File file = new File(path);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(buf);
            fw.close();
            return true;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 缓冲输出字符流(推荐) */
    public boolean bufferedWriterWrite(String path, char[] buf){
        File file = new File(path);
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(buf);
            bw.close();
            return true;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * write a file content to another file by using stream
     * fit for unreadable files
     * @param buffered suggest true, which will use high performance BufferedInputStream and BufferedOutputStream
     * */
    public boolean streamFile2File(String from, String to, boolean buffered){
        File readFile = new File(from);
        File writeFile = new File(to);
        int len;
        try {
            FileInputStream fis = new FileInputStream(readFile);
            FileOutputStream fos = new FileOutputStream(writeFile);
            if (! buffered) {
                byte[] buf = new byte[1024];
                while ((len = fis.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
            } else {
                BufferedInputStream bis = new BufferedInputStream(fis);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                while ((len = bis.read()) != -1) {  // read()方法  返回读取的字符，末尾返回-1，bis是把内容读到自己的肚子里了
                    bos.write(len);
                    bos.flush();  // 一定要刷上去，非常关键
                }
            }
            fos.close();  // 关闭了FileOutputStream也同时关闭了BufferedOutputStream
            fis.close();  // 关闭了FileInputStream也同时关闭了BufferedInputStream
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /** 加密和解密文件
     * used to encode a file or decode a file by using a function as a coder
     * the function will encode each byte in the file with the given algorithm in the coder.apply() method
     *
     * */
    public boolean codeFile2File(String from, String to, Function<Integer, Integer> coder){
        File readFile = new File(from);
        File writeFile = new File(to);
        int len;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(readFile));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(writeFile));
            while ((len = bis.read()) != -1) {  // read()方法  返回读取的字符，末尾返回-1，bis是把内容读到自己的肚子里了
                bos.write(coder.apply(len));  // 针对每一个byte进行加密操作
                bos.flush();  // 一定要刷上去，非常关键
            }
            bos.close();  // 关闭了FileOutputStream也同时关闭了BufferedOutputStream
            bis.close();  // 关闭了FileInputStream也同时关闭了BufferedInputStream
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * write a readable(char) file content to another file by using FileReader and FileWriter
     * fit for unreadable files
     * @param buffered suggest true, which will use high performance BufferedInputStream and BufferedOutputStream
     * */
    public boolean readableFile2File(String from, String to, boolean buffered) {
        File readFile = new File(from);
        File writeFile = new File(to);
        try {
            FileReader fr = new FileReader(readFile);
            FileWriter fw = new FileWriter(writeFile);
            int len;
            if (! buffered) {
                char[] buf = new char[1024];
                while ((len = fr.read(buf)) != -1) {
                    fw.write(buf, 0, len);
                }
            } else {
                String line;
                BufferedReader br = new BufferedReader(fr);
                BufferedWriter bw = new BufferedWriter(fw);
                while ((line = br.readLine()) != null) {
                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                }
            }
            fw.close();
            fr.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * SequenceStream merge
     * 使用
     * fileSystem.merge(Arrays.asList(strPath1, strPath2, ...), strPathOut)
     * */
    public boolean merge(Iterable<String> fromPaths, String toPath){
        File writeFile = new File(toPath);
        try {
            Vector<FileInputStream> vector = new Vector<>();
            Iterator<String> it = fromPaths.iterator();
            while (it.hasNext()) vector.add(new FileInputStream(new File(it.next())));
            SequenceInputStream sis = new SequenceInputStream(vector.elements());
            FileOutputStream fos = new FileOutputStream(writeFile);
            int len;
            byte[] buf = new byte[1024];
            while ((len = sis.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.close();
            sis.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * write object
     * */
    public boolean writeObject (Serializable obj, String path) {
        Class c = obj.getClass();
        try {
            Field field = c.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            System.out.println("field serialVersionUID is required");
            e.printStackTrace();
            return false;
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)));
            oos.writeObject(obj);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * read object
     * then you need convert to any class instance
     * */
    public Object readObject (String path) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)));
            return ois.readObject();
        } catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
            return null;
        }
    }



}
