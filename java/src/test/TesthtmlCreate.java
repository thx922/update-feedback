import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ike on 16-9-29.
 */
public class TesthtmlCreate {
    @Test
    public void test(){
        String filePath ="/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceApiDoc/xxx";
        File f = new File(filePath);
        if(!f.isDirectory()){
            f.mkdir();
        }
        String templateContent="<html>12121212</html>" ;//组装html字符串
        String fileame = "/生成html文件.html";
        fileame = filePath+fileame;//生成的html文件保存路径。
        FileOutputStream fileoutputstream = null;// 建立文件输出流
        try {
            fileoutputstream = new FileOutputStream(fileame);
            System.out.print("文件输出路径:");
            System.out.print(fileame);
            byte tag_bytes[] = templateContent.getBytes();
            fileoutputstream.write(tag_bytes);
            fileoutputstream.close();//关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2(){
        String pty ="java.util.List<java.util.Map<java.lang.String,  java.lang.Object>>  list";
        int left = pty.lastIndexOf(" ");
        pty = pty.substring(0, left);
//        + pty.substring(left + 1);
        System.out.println("空格位置:"+left);
    }
}
