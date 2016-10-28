package csio.client;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by ike on 16-10-19.
 */
public class testMD5 {
    @Test
    public void test(){
        File file = new File("/home/ike/java/java/feedback/java/src/main/java/dnn/web1/admin/AdminAction1.java");
        System.out.println(getFileMD5(file));
        //44f672d907ff0b09d9f14d4d1e55f2f3
        //27e7d18c01f79ca01ae04cc20fe7d217
    }
    public  String getFileMD5(File file) {
        if (!file.isFile()){
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in=null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
