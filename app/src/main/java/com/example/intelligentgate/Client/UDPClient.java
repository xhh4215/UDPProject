package com.example.intelligentgate.Client;

import android.app.Activity;
import android.nfc.Tag;
import android.util.Log;


import com.example.intelligentgate.UDPConfig.UDPResource;
import com.example.intelligentgate.Utils.StringToByte;
import com.example.intelligentgate.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    //设置接收数据的超时时间
    private static final int TIMEOUT = 5000;
    private Activity activity;
    private static final int MAXNUM = 5; // 设置重发数据的最多次数


    public UDPClient(Activity activity) {
        this.activity = activity;
    }

    public void JavaTest() {
        String str_send = "Hello UDPserver";
        byte[] buf = new byte[1024];
        try {
            // 客户端在9000端口监听接收到的数据
            DatagramSocket ds = new DatagramSocket(9000);
            // 获取本地的网络地址对象
            InetAddress loc = InetAddress.getByName("172.168.2.102");

            // 定义用来发送数据的DatagramPacket实例
            DatagramPacket dp_send = new DatagramPacket(str_send.getBytes(), str_send.length(), loc, 3000);
            // 定义用来接收数据的DatagramPacket实例
            DatagramPacket dp_receive = new DatagramPacket(buf, 1024);
            // 数据发向本地3000端口
            // 设置接收数据时阻塞的最长时间
            ds.setSoTimeout(TIMEOUT);
            // 重发数据的次数
            int tries = 0;
            // 是否接收到数据的标志位
            boolean receivedResponse = false;
            // 直到接收到数据，或者重发次数达到预定值，则退出循环
            while (!receivedResponse && tries < MAXNUM) {
                // 发送数据
                ds.send(dp_send);
                try {
                    // 接收从服务端发送回来的数据
                    ds.receive(dp_receive);
                    // 如果接收到的数据不是来自目标地址，则抛出异常
                    if (!dp_receive.getAddress().equals(loc)) {
                        throw new IOException("Received packet from an umknown source");
                    }
                    // 如果接收到数据。则将receivedResponse标志位改为true，从而退出循环
                    receivedResponse = true;
                } catch (InterruptedIOException e) {
                    // 如果接收数据时阻塞超时，重发并减少一次重发的次数
                    tries += 1;
                    System.out.println("Time out," + (MAXNUM - tries) + " more tries...");
                }
            }
            if (receivedResponse) {
                // 如果收到数据，则打印出来
                System.out.println("client received data from server：");
                String str_receive = new String(dp_receive.getData(), 0, dp_receive.getLength()) + " from "
                        + dp_receive.getAddress().getHostAddress() + ":" + dp_receive.getPort();
                System.out.println(str_receive);
                EventBus.getDefault().post(new MessageEvent(str_receive));
                Log.d("TAG", "数据已发送");

                // 由于dp_receive在接收了数据之后，其内部消息长度值会变为实际接收的消息的字节数，
                // 所以这里要将dp_receive的内部消息长度重新置为1024
                dp_receive.setLength(1024);
            } else {
                // 如果重发MAXNUM次数据后，仍未获得服务器发送回来的数据，则打印如下信息
                System.out.println("No response -- give up.");
            }
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendUDPData(String message, int port, String ip) {
        byte[] send_message = StringToByte.toBytes(message);
        try {
            DatagramSocket ds = new DatagramSocket(port);
            // 获取本地的网络地址对象
            InetAddress loc = InetAddress.getByName(ip);
            // 定义用来发送数据的DatagramPacket实例
            DatagramPacket dp_send = new DatagramPacket(send_message, send_message.length, loc, port);
            // 设置接收数据时阻塞的最长时间
            ds.setSoTimeout(TIMEOUT);
            // 发送数据
            ds.send(dp_send);
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void receiveUDPData(int port) {
         try {
             byte[] buf = new byte[80];
             // 服务端在3000端口监听接收到的数据
             DatagramSocket ds = new DatagramSocket(8705);
             // 接收从客户端发送过来的数据
             DatagramPacket dp_receive = new DatagramPacket(buf, 80);
             System.out.println("server is on，waiting for client to send data......");
             boolean f = true;
             while (f) {
                // 服务器端接收来自客户端的数据
                ds.receive(dp_receive);
                System.out.println("server received data from client：");
                String str_receive = StringToByte.bytesToHexFun1(dp_receive.getData());
                System.out.println(str_receive);
                EventBus.getDefault().post(new MessageEvent(str_receive));
                 // 所以这里要将dp_receive的内部消息长度重新置为1024
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
