package csio.service;

import csio.client.FilePath;
import csio.client.WebPath;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ike on 16-10-15.
 */
public class Server {
    @Test
    public void test() throws IOException{
        FilePath filePath = new FilePath();
//        filePath.setServerIndexHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc");
        filePath.setServerIndexHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc");
        serverStart(filePath.getServerIndexHtmlPath());
    }

    public void serverStart(String serverIndexHtmlPath )throws IOException {
        int port = 8899;

        final ServerSocket server = new ServerSocket(port);
        System.out.println("等待与客户端建立连接...");

        File f = new File(serverIndexHtmlPath);
        String[] flist = f.list();
        IndexTask indexTask = new IndexTask();
        indexTask.createindex(serverIndexHtmlPath, flist, null);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    Socket socket = null;
                    try {
                        socket = server.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 每接收到一个Socket就建立一个新的线程来处理它
                    new Thread(new Task(socket  ,serverIndexHtmlPath )).start();

                }
            }
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {

        }
    }

    /**
     *
     * @param //serverIndexHtmlPath  api文档服务器放 jsp 的地址
     *                             serverIndexHtmlPath="/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc"
     * @throws IOException
     */
    /*public void serverStart( String serverIndexHtmlPath)throws IOException {
        // 为了简单起见，所有的异常信息都往外抛
        int port = 8899;
        // 定义一个ServiceSocket监听在端口8899上

        final ServerSocket server = new ServerSocket(port);
        System.out.println("等待与客户端建立连接...");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    Socket socket = null;
                    try {
                        socket = server.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 每接收到一个Socket就建立一个新的线程来处理它
                    new Thread(new Task(socket)).start();
                    File f  = new File(serverIndexHtmlPath);
                    String [] flist = f.list();
                    IndexTask indexTask = new IndexTask();
                    indexTask.createindex( serverIndexHtmlPath ,flist);
                    try {
                        server.setSoTimeout(1000*60*3);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {

        }
    }*/

}
