package csio;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ike on 16-10-15.
 */
public class serverTask implements Runnable {

    private Socket socket;
    private int serverQuired;
    private List<String> guideHtmlName = new ArrayList<>();

    /**
     * 构造函数
     */
    public serverTask(Socket socket , int serverQuired) {
        this.socket = socket;
        this.serverQuired = serverQuired;
    }

    @Override
    public void run() {
        try {
            handlerSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跟客户端Socket进行通信
     *
     *
     */
    private void handlerSocket() throws Exception {
//        DataInputStream dis =  new DataInputStream(socket.getInputStream());
//        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//
//        String status = dis.readUTF();
//        if(status.equals("live")){
//            if(serverQuired == 1){//服务器确定更新
//                dos.writeUTF("update-yes");
//            }else{
//                dos.writeUTF("update-no");
//            }
//        }else {
//            serverQuired = 0;
//            dos.writeUTF("receive-update-yes");
//            //update list
//        }



        BufferedReader br = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String temp;
        int index;
        while ((temp = br.readLine()) != null) {
//            if ((index = temp.indexOf("eof")) != -1) { // 遇到eof时就结束接收
//                sb.append(temp.substring(0, index));
//                break;
//            }
            sb.append(temp);
        }


       /* String str = sb.toString();
        if(StringUtils.isNotEmpty(str) && StringUtils.isNotBlank(str)){
            str = str.substring(1,str.length()-1);
            String [] strs = str.split(",");
            if(strs != null && strs.length >0){
                for(String ss : strs){
                    String path = dirCreate(ss);
                    String content = contentRead(ss);
                    htmlCreate(path.toString() , content.toString());
                }
            }
        }*/

        String status = sb.toString();
        String response ="";
        if(status.equals("live")){
            if(serverQuired == 1){//服务器确定更新
//                dos.writeUTF("update-yes");
                response ="update-yes";
            }else{
//                dos.writeUTF("update-no");
                response ="update-no";
            }
        }else {
            serverQuired = 0;
//            dos.writeUTF("receive-update-yes");
            response ="receive-update-yes";
            //update list
        }

        // 回应一下客户端
        Writer writer = new OutputStreamWriter(socket.getOutputStream(),
                "UTF-8");
//        writer.write(String.format("Hi,%d.天朗气清，惠风和畅！", socket.getPort()));
        writer.write(response);
        writer.flush();
        writer.close();
        System.out.println(
                "To Cliect[port:" + socket.getPort() + "] 回复客户端的消息发送成功"+response);

//        br.close();
        socket.close();
    }

    public String dirCreate( String readStr){
        int indexs =readStr.trim().lastIndexOf("/WEB-INF/");
        String pathSuffix = readStr.trim().substring(indexs+9,readStr.trim().length());//TODO : 文件生成路径
        String pathPre = readStr.trim().substring(0,indexs);
        String [] pathSuffixSplits =pathSuffix.split("/");
        StringBuffer path = new StringBuffer(pathPre);
        for(String pathSuffixSplit :pathSuffixSplits){ //建立目录
            if(pathSuffixSplit.contains(".")){
                path.append("/"+pathSuffixSplit);
            }else{
                path.append("/"+pathSuffixSplit);
                File f = new File(path.toString());
                if(!f.isDirectory()){
                    f.mkdir();
                }
            }
        }
        return path.toString();
    }

    public String contentRead(String filePath) throws IOException{
        BufferedReader bis = new BufferedReader(new FileReader(filePath.trim()));
        StringBuilder sbs = new StringBuilder();
        List<String> lines = new ArrayList<String>();
        while (bis.read() != -1) {
            lines.add(bis.readLine());
        }
        for (String s : lines) {
            sbs.append(s);
        }
        return sbs.toString();
    }

    public void htmlCreate(String path , String content){
        FileOutputStream fileoutputstream = null;// 建立文件输出流
        try {
            fileoutputstream = new FileOutputStream(path);
            byte tag_bytes[] = content.getBytes();
            fileoutputstream.write(tag_bytes);
            fileoutputstream.close();//关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
