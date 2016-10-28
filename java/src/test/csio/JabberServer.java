package csio;

import dnn.web.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ike on 16-10-15.
 */
public class JabberServer {
    private String entityPrePath = "dnn.web";
    private String filePath = "/home/ike/java/java/feedback/java/src/main/java/dnn/web";
    private String htmlFilePath = "/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc";
//    dnn.web.InterfaceDoc it = new InterfaceDoc();
    // Choose a port outside of the range 1-1024:
    public static final int PORT = 8088;
    @Test
    public void test() throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Started: " + s);

        try {
            Socket socket = s.accept();
            try {
                System.out.println(
                        "Connection accepted: "+ socket);
                BufferedReader KeyIn = new BufferedReader(new InputStreamReader(System.in));
//                BufferedReader KeyIn = new BufferedReader(new InputStreamReader(is));

                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));
                // Output is automatically flushed
                // by PrintWriter:
                PrintWriter out =
                        new PrintWriter(
                                new BufferedWriter(
                                        new OutputStreamWriter(
                                                socket.getOutputStream())),true);
                String str="";
                while (in.read() != -1) {
                    str = in.readLine();
                    if (str.equals("END")) break;
                    System.out.println("Client: " + str);
//                    out.println(KeyIn.readLine());

                }
                if(StringUtils.isNotEmpty(str) && StringUtils.isNotBlank(str)){
                    str = str.substring(0,str.length()-1);
                    String [] strs = str.split(",");
                    if(strs != null && strs.length >0){
                        for(String ss : strs){
                            File f =new File(ss.trim());
                            if( !f.isDirectory()){
                                f.mkdir();
                            }
                        }
                    }
                }
                // Always close the two sockets...
            } finally {
                System.out.println("关闭了closing...");
                socket.close();
            }
        } finally {
            s.close();
        }
    }
}
