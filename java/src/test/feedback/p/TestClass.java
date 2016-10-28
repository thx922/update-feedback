package feedback.p;

import dnn.web.custom.feedbackInfo.FeedbackInfoAct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by ike on 16-9-13.
 */
public class TestClass {

    public static void main(String[] args) {
        FeedbackInfoAct fct =new FeedbackInfoAct();
        Annotation [] annotations=fct.getClass().getAnnotations();
        for (Annotation annotation :annotations){
            System.out.println(annotation);
        }
        System.out.println(fct.getClass().getCanonicalName());

        Method [] methods= fct.getClass().getMethods();
        for(Method method:methods){
            System.out.println("方法:"+method.getName());
            Annotation [] annot =method.getAnnotations();
            for(Annotation annotate :annot){
//                System.out.println("得到注视::"+annotate);
//                System.out.println("得到注视::"+annotate.annotationType());
//                System.out.println("得到注视::"+annotate.getClass().getTypeParameters());

                //@org.springframework.web.bind.annotation.
                // PostMapping(headers=[], path=[], produces=[], name=, params=[], value=[save], consumes=[])
            }

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> clas : parameterTypes) {
                String parameterName = clas.getName();
                System.out.println("参数名称:" + parameterName);
            }
            System.out.println("======================================");
        }
        System.out.println("报名:"+fct.getClass().getName());//dnn.web.custom.feedbackInfo.FeedbackInfoAct


        /*try {
            Method m=fct.getClass().getMethod("save",String.class);
            annotations =m.getAnnotations();
            for(Annotation an :annotations){
                System.out.println("得到注视::"+an);
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/





    }
}
