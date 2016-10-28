package csio.client;

import dnn.web.InterfaceDoc;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ike on 16-10-15.
 */
public class Client {
    public static void main(String[] args) throws Exception {
        final int heartFlag =1;//1发送心跳 ,0不发
        Socket clients = new Socket("127.0.0.1", 8899);
        System.out.println("建立连接....");

        FilePath filePath = new FilePath();
        filePath.setWeProjectbName("feedback");
        filePath.setFileList("/home/ike/java/java/feedback/java/src/main/java/dnn/web1");
        filePath.setEntityList("dnn.web1");
        filePath.setClientHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc");

        InterfaceDoc interfaceDoc = new InterfaceDoc();
        JspdocContentSend.sendJspContent( interfaceDoc ,filePath , null);
//        JspdocContentSend.AllFileAndContent(clients , filePath.getClientHtmlPath() ,filePath.getWeProjectbName() );;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {  //定时发送心跳
            @Override
            public void run() {
                new Thread(new ClientHeartTask(clients ,filePath )).start();
            }
        },0, 1000*20);


//        Timer timerdocCreate = new Timer();
//        timerdocCreate.schedule(new TimerTask() { //文档检查更新
//            @Override
//            public void run() {
//                new Thread(new ClientDocCheckTask(clients ,filePath)).start();
//            }
//        },0, 1000*60*20);

    }





    /**
     *三个参数都必须要
     * @param webFilePath     web文件路径 ,如:
     *                    list.add("/home/ike/java/java/feedback/java/src/main/java/dnn/web");
     *                    list.add("/home/ike/java/java/feedback/java/src/main/java/dnn/web1");
     * @param webPackageName  对应上面webFilePath的文件包名
     *                        list.add("dnn.web");
     *                        list.add("dnn.web1");
     * @param cilentHtmlPath  各个应用本地html路径地址
     *                        cilentHtmlPath ="/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc"
     */
    /*public void clientStart(List<String> webFilePath ,List<String> webPackageName, String cilentHtmlPath){
        InterfaceDoc interfaceDoc = new InterfaceDoc();
        for (int i = 0; i < webFilePath.size(); i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    try {
                        List<String> list =interfaceDoc.FirstMethod(webFilePath.get(i).toString(),webPackageName.get(i).toString() ,cilentHtmlPath);
                        TestClient testclient = TestClientFactory.createClient();
                        testclient.send(String.format(list.toString(), testclient.clients.getLocalPort()));
                        testclient.receive();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                }
//            }).start();
        }
    }*/


}

