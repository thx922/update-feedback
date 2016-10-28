import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by ike on 16-10-18.
 */
public class Testxintiao {
    @Test
    public  void test(){
        File f = new File ("/home/ike/java/java/feedback/java/src/main/java/dnn/web/custom/feedbackInfo/FeedbackInfoAct.java");
        try {
            System.out.print(FileUtils.checksumCRC32(f));//149636390
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
