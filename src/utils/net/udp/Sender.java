package utils.net.udp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 基于UDP协议的通讯：收发双方要用DatagramSocket类的对象，相当于是个移动终端
 仅发送(只是一个扩音器)：DatagramSocket() 无参构造函数，
 收发结合(相当于手机)：DatagramSocket(int port)监听自己的端口。相互交流的时候选这个，双方都要监听自己的端口
 * 交流双方需要datagramSocket(自己的端口)，定义两个packet对象, 一个收dp_recv, 一个发dp_send
 * */
public class Sender {//对话发起者
    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket(9090);//ds就是个手机，监听自己的端口
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//接受键盘输入
        while (true) {
            System.out.print("请发言：");
            String content = br.readLine();//写入内容
            if ("q".equals(content)) break;//如果写个q就结束通话
            DatagramPacket dp_send = new DatagramPacket(content.getBytes(), content.getBytes().length, InetAddress.getLocalHost(), 9091);//给9091端口发信息
            ds.send(dp_send);
            System.out.println("请等待...");
            byte[] buf = new byte[1024];
            DatagramPacket dp_recv = new DatagramPacket(buf, buf.length);
            ds.receive(dp_recv);
            System.out.println("对方：" + new String(buf, 0, dp_recv.getLength()));
        }
        ds.close();
    }
}
