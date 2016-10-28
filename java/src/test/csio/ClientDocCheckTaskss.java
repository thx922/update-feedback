/*
package csio;

import csio.client.FileMD5;
import csio.client.FilePath;
import csio.client.JspdocContentSend;
import dnn.web.InterfaceDoc;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by ike on 16-10-19.
 *//*

public class ClientDocCheckTask implements Runnable {
    private FilePath filePaths;
    private Socket clients;

    public ClientDocCheckTask(Socket client , FilePath filePath) {
        this.clients = client;
        this.filePaths = filePath;
    }

//    filePath.setFileList("/home/ike/java/java/feedback/java/src/main/java/dnn/web1");
//        filePath.setEntityList("dnn.web1");
//        filePath.setClientHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc");
    //ServerIndexHtmlPath(           "/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc");
    @Override
    public void run() {
        String projectName = filePaths.getWeProjectbName();
        String webFilePath = filePaths.getFileList();
        String filePackage = filePaths.getEntityList();
        String htmlPath = filePaths.getClientHtmlPath();

        System.out.println("不定时更新减肥品");
        InterfaceDoc interfaceDoc = new InterfaceDoc();
        interfaceDoc.FirstMethod(projectName,webFilePath ,filePackage ,htmlPath , null );
        System.out.println("减肥品2");

        List<String> list = new ArrayList<>();
        List<String> listnewDocName = new ArrayList<>();
        File file = new File(htmlPath);
        list = getAllFile(file, list);
        try {
            for (String str : list) {
//                if(){
//
//                }
                String filePackagetoPath = filePackage.replace(".", "/");
                filePackagetoPath = str.substring(str.indexOf(filePackagetoPath), str.lastIndexOf(".")).trim();
                String[] guideJspFlag = filePackagetoPath.split("/");
                if (guideJspFlag.length > 2) {//说明不是guide.jsp
                    String htmlPathtxt = htmlPath + "/" + filePackagetoPath + ".txt";
                    String txtContent = readDoc(htmlPathtxt);
                    FileMD5 fileMD5 = new FileMD5();
                    String javaFileChange = fileMD5.getFileMD5(new File(str));
                    if (!javaFileChange.equals(txtContent)) {
                        listnewDocName.add(str);
                        //重新写入修改后的md5
                        writeDoc(htmlPathtxt, javaFileChange);
                    }
                }
            }
            String jspDoc ="none";
//            if(listnewDocName !=null && listnewDocName.size()>0 ){ //表示有新的改变
//                jspDoc = JspdocContentSend.sendJspContent(interfaceDoc, filePaths, listnewDocName);
//            }
//            writeToClient(getGuidejsp(jspDoc, htmlPath));
            writeToClient(jspDoc);//回送给服务器
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToClient( String jspContent) throws  IOException{
        OutputStream os = clients.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeUTF("send-hasUpdate"+jspContent);
    }

    public void writeDoc(String path ,String newFileMD5) throws IOException{
        BufferedWriter bos = new BufferedWriter(new FileWriter( path ));
        StringBuilder sb = new StringBuilder("");
        bos.write(sb.toString());
        bos.write(newFileMD5+"-date:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        bos.close();
    }

    public String readDoc(String path) throws IOException{
        BufferedReader bis = new BufferedReader(new FileReader( path ));
        StringBuilder sb = new StringBuilder();
        List<String> lines = new ArrayList<String>();
        while (bis.read() != -1) {
            lines.add(bis.readLine());
        }
        for (String s : lines) {
            sb.append(s);
        }
        String[] strmd5 = sb.toString().split("-date:");
        return strmd5[0];
    }
    public List<String> getAllFile(File file ,List<String> list ){
        File [] files = file.listFiles();
        for(File f : files){
            if(!f.isDirectory()){
                list.add(f.getPath());
            }else{
                getAllFile(f,list);
            }

        }
        return list;
    }


}
*/
