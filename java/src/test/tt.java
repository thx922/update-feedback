import com.sun.javadoc.*;
import com.sun.tools.javadoc.RootDocImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ike on 16-9-21.
 */
public class tt  extends Doclet{
    @Test
    public void t(){
        String InitProducts[] = {"商品01","商品05","商品04","商品02","商品03"};
        String deleteProducts[] = {"商品04","商品02"};

        Object object = new Object();
        Map<String, Object> map = new HashMap<String, Object>();

        for(String init :InitProducts)
        {
            map.put(init, object);
        }

        for(String delete: deleteProducts)
        {
            if(map.get(delete)!= null)
                map.remove(delete);
        }

        String[] newProducts = new String[map.size()];
        map.keySet().toArray(newProducts);

        System.out.println(Arrays.toString(newProducts));
        System.out.println(newProducts);


    }
    @Test
    public void test2(){

    }

    public static boolean start(RootDoc root){
        String tagName = "mytag";
        writeContents(root.classes(), tagName);
        return true;
    }

    private static void writeContents(ClassDoc[] classes, String tagName) {
        for (int i=0; i < classes.length; i++) {
            boolean classNamePrinted = false;
            MethodDoc[] methods = classes[i].methods();
            for (int j=0; j < methods.length; j++) {
                Tag[] tags = methods[j].tags(tagName);
                if (tags.length > 0) {
                    if (!classNamePrinted) {
                        System.out.println("\n" + classes[i].name() + "\n");
                        classNamePrinted = true;
                    }
                    System.out.println(methods[j].name());
                    for (int k=0; k < tags.length; k++) {
                        System.out.println("   " + tags[k].name() + ": "
                                + tags[k].text());
                    }
                }
            }
        }
    }


    /**
     * 测试tag
     * @return
     */
    public String testtag(){
        System.out.println("发hi为");
        return "look";
    }


}
