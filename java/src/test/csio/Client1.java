package csio;

import csio.client.TestClient;
import csio.client.TestClientFactory;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

/**
 * Created by ike on 16-10-15.
 */
public class Client1 {
//    public Socket clients;
    private Writer writer;

    private DataInputStream reader;

    @Test
    public void test() throws IOException {
        Socket clients = new Socket("127.0.0.1", 8899);
        
    }

    public static void clientStart(List<String> webFilePath ,List<String> webPackageName, String cilentHtmlPath){
        try {
            TestClient testclient = TestClientFactory.createClient();
            //处理心跳线程
            while (true) {
                testclient.status();
            }
            //处理文档检测线程
//                testclient.docCheck();
            //处理文档传输
//                testclient.send(String.format("client发送消息", testclient.clients.getLocalPort()));
//                testclient.receive();
//                }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

