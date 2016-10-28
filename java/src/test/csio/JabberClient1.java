package csio;

import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by ike on 16-10-15.
 */
public class JabberClient1 {
    @Test
    public void test() throws IOException {
        InetAddress addr =
                InetAddress.getByName("127.0.0.1");
        System.out.println("addr = " + addr);
        Socket socket =
                new Socket(addr, JabberServer.PORT);
        try {
            System.out.println("socket = " + socket);
            BufferedReader KeyIn = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));
            PrintWriter out =
                    new PrintWriter(
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            socket.getOutputStream())),true);
//          for(int i = 0; i < 10; i ++) {
//            out.println("howdy " + i);
//            String str = in.readLine();
//            System.out.println(str);
//          }
//          out.println("END");
            String str =null;
            while(true)
            {
                str = KeyIn.readLine();
                if("END".equals(str))
                    break;
                out.println(str);
                System.out.println("Server:"+in.readLine());


            }
        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }
}