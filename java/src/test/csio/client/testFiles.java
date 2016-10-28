package csio.client;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ike on 16-10-19.
 */
public class testFiles {

    @Test
    public void test(){
        List<String> list = new ArrayList<>();
//        File file = new File("/home/ike/java/java/feedback/java/src/main/java/dnn/web1");
        File file = new File("/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc");
        list = getAllFile(file ,list);
//        System.out.println(list);

        String s="8ca2b665ab424e1264b8a5348caf7643-date:2016/06/22 09:22:13";
        String [] sp =s.trim().split("-date:");
        System.out.println(sp.length);

        File f = new File("XXT.txt");
       System.out.println( f.exists()  );
        f.getPath();


        System.out.println(System.currentTimeMillis());
        System.out.println(LocalDateTime.now());
        System.out.println(LocalDate.now());

        System.out.println( LocalDateTime.now().getYear()+"/"+LocalDateTime.now().getMonthValue()+"/"
                +LocalDateTime.now().getDayOfMonth()+"  "+LocalDateTime.now().getHour()+":"
                +LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond() );

        String _pattern = "yyyy/MM/dd HH:mm:ss";
//        SimpleDateFormat format = new SimpleDateFormat(_pattern);
//        System.out.println(format.format( LocalDateTime.now()));
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

//        System.out.println(LocalDateTime.parse(LocalDateTime.now().toString(), DateTimeFormatter.ofPattern(_pattern)));
        String status ="send-hasUpdate"+"none";
        String[] str = status.split("send-hasUpdate");
        System.out.println(str);

    }

    @Test
    public void test2() throws IOException{
        String path ="/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc/dnn/web1/user/UserAct1.txt";
        //a93b0012802e89bc91a7db661be7a1e5
        String newFIleMD5 = "dfgfget645rcv";
        writeDoc(path,newFIleMD5);
    }
    public void writeDoc(String path ,String newFileMD5) throws IOException {
        BufferedWriter bos = new BufferedWriter(new FileWriter( path ));
        StringBuilder sb = new StringBuilder("");
        bos.write(sb.toString());
        bos.write(newFileMD5);
        bos.close();
    }

    public List<String> getAllFile(File file , List<String> list ){
        File [] files = file.listFiles();
        for(File f : files){
            if(!f.isDirectory() ){
                if(  !f.getName().contains(".txt")){
                    System.out.println( f.getName() +"炒股:"+f.getPath());
                }
                list.add(f.getPath());
            }else{
                getAllFile(f,list);
            }

        }
        return list;
    }

    @Test
    public void Test3(){
        long d = System.currentTimeMillis();
        System.out.println("开始:"+System.currentTimeMillis());
        int [] arr = new int[9999999];
        for(int i=0;i<9999999;i+=3){
            arr[i] =i;
            arr[i+1] =i+1;
            arr[i+2] =i+2;
        }
        long sd = System.currentTimeMillis();
        System.out.println("借宿:"+sd);
        System.out.println(sd-d);
    }

    @Test
    public void Test4(){
        byte[] bt= new byte[1024];
//        String ss ="";
//        bt=ss.getBytes();
        System.out.println(bt.length);
        // e92f1173baf0c22640087b9b6274d08e-date:2016/10/25 08:58:09

    }

    @Test
    public void test45(){
        String ss="guide.jsp";
        String [] d = ss.split("guide.jsp");
        System.out.println(d.length);
    }
}
