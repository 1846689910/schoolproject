package utils.net.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
/**
 * 需要socket(对方主机,端口), 一旦被accept, 用socket的两个get方法来读写也就是收发
 * */
public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getLocalHost(), 9090);//连接的主机对象和通话的端口
        InputStream is = socket.getInputStream();
        File f = new File("E:\\misc1.txt");
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f, true));//
        int length = 0;
        while ((length = bis.read()) != -1) {
            bos.write(length);
        }
        bos.close();
        bis.close();
        socket.close();
    }
}
