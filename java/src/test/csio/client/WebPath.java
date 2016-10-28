package csio.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ike on 16-10-15.
 */
public class WebPath {

    public String fileList (){
        String list = "/home/ike/java/java/feedback/java/src/main/java/dnn/web1";
//        list.add("/home/ike/java/java/feedback/java/src/main/java/dnn/web2");
        return list;
    }

    public String entityList (){
        String list = "dnn.web1";
//        list.add("dnn.web2");
        return list;
    }

    public String clientHtmlPath(){
        String htmlFilePath = "/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc";
        return htmlFilePath;
    }
    public String serverIndexHtmlPath(){
        String indexhtmlpath="/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc";
        return indexhtmlpath;
    }

}
