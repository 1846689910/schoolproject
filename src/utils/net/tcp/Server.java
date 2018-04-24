package utils.net.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP主用于文件传输, 如果一定要对话用socket.两个get得到is和os来读写收发(不用while(-1)循环判断结尾,因为都是一句一句交流的)
 方法1:is.read(buf)接收, 再new String(buf,0,length); is.write(buf)发送；
 方法2:通过is得到bufferedReader来readLine收, 用os得到printStream来println发
 服务端：需要serverSocket(自己端口),用accept()获取socket, 用socket的两个get方法来读写也就是收发
 * */
public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(9090);  // 服务器监听9090端口
        Socket socket = ss.accept();  // 等待客户端来连接
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());  // 将ss.getOutputStream转为缓冲流
        File f = new File("E:\\a.txt");  // 获得文件句柄
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));  // 用缓冲流读文件
        int length = 0;
        while ((length = bis.read()) != -1) {
            bos.write(length);
            bos.flush();  // 服务端这里一定要及时刷到硬盘上，最关键的一点，否则写不上去的，客户端不用flush
        }
        bis.close();  // 从内向外依次关闭资源
        bos.close();
        ss.close();
    }
}
