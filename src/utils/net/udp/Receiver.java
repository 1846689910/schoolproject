package utils.net.udp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver {
    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket(9091);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("请等待...");
            byte[] buf = new byte[1024];
            DatagramPacket dp_recv = new DatagramPacket(buf, buf.length);
            ds.receive(dp_recv);
            System.out.println("对方：" + new String(buf, 0, dp_recv.getLength()));
            System.out.print("请输入：");
            String content = br.readLine();
            if ("q".equals(content))break;
            DatagramPacket dp_send = new DatagramPacket(content.getBytes(), content.getBytes().length, InetAddress.getLocalHost(), 9090);//给9090端口发信息
            ds.send(dp_send);
        }
        ds.close();
    }
}
