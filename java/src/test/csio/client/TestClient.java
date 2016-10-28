package csio.client;

import dnn.web.InterfaceDoc;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ike on 16-10-15.
 */
public class TestClient {
    /**
     * 构造函数
     * @param host 要连接的服务端IP地址
     * @param port 要连接的服务端对应的监听端口
     * @throws Exception
     */
    public TestClient(String host, int port) throws Exception {
        // 与服务端建立连接
        this.clients = new Socket(host, port);
        System.out.println("Cliect[port:" + clients.getLocalPort() + "] 与服务端建立连接...");
        //心跳
    }

    public Socket clients;

    private Writer writer;

    private DataInputStream reader;

    public void status(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                        reader = new DataInputStream(clients.getInputStream());
                        if (writer == null) {
                            System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息libe");
                            writer = new OutputStreamWriter(clients.getOutputStream(), "UTF-8");
                            writer.write("live");//心跳
                        } else if (reader.readUTF().equals("update-yes")) {
                            System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息文档");
                            //发送文档
                        } else if (reader.readUTF().equals("update-no")) {
                            System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息发送成功liveuno");
                            writer.write("live");//心跳
                        } else if (reader.readUTF().equals("receive-update-yes")) {
                            System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息发送成功");
//                        reader.close();
//                        writer.close();
                        }
                        writer.flush();// 写完后要记得flush
                        try {
                            Thread.sleep(1000 * 2); //2秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }).start();
    }

    public void docCheck(){

    }

    /**
     * 发送消息
     * @param msg
     * @throws Exception
     */
    public void send(String msg) throws Exception {
        // 建立连接后就可以往服务端写数据了
        if(writer == null) {
            writer = new OutputStreamWriter(clients.getOutputStream(), "UTF-8");
        }
        writer.write(msg);
        writer.write("eof\n");
        writer.flush();// 写完后要记得flush
        System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息发送成功");
    }
    /**
     * 接收消息
     * @throws Exception
     */
    public void receive() throws Exception {
        // 写完以后进行读操作
        Reader reader = new InputStreamReader(clients.getInputStream(), "UTF-8");
//        BufferedReader in =
//                new BufferedReader(
//                        new InputStreamReader(
//                                clients.getInputStream(),"UTF-8"));
        // 设置接收数据超时间为10秒
        clients.setSoTimeout(20*1000);
        char[] chars = new char[64];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = reader.read(chars)) != -1) {
            sb.append(new String(chars, 0, len));
        }

        System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息收到了，内容:" + sb.toString());
        reader.close();

        // 关闭连接
        writer.close();
        clients.close();
    }

}
