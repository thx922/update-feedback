import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ike on 16-9-14.
 */
public class TestAct2 {
    private ClassLoader classLoader;

    @Test
    public void test2() {
//        String entityPrePath = "com";
//        String filePath = "/home/ike/java/java/japi/java/src/main/java/com/dounine/japi/web";
        String entityPrePath = "dnn";
        String filePath = "/home/ike/java/java/feedback/java/src/main/java/dnn/web";
        File file = new File(filePath);
        String[] names = file.list();

        for (String s : names) {
            String[] strings = s.split(".java");
            Class<?> demo1 = null;
            List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> getAnnoInfo = anno(filePath, strings[0]);//解析注释
            try {
                demo1 = Class.forName("dnn.web." + strings[0]);
                Method[] dan = demo1.getMethods();//获取所有方法
                for (Method method : dan) {
//                    System.out.println("方家鸥额饿哦发热无:"+method.getName());//wait,equals,toString,hashCode,getClass,notify,notifyAll
                    String methodName = method.getName();
                    if ("wait".equals(methodName)) {
                        continue;
                    } else if ("equals".equals(methodName)) {
                        continue;
                    } else if ("toString".equals(methodName)) {
                        continue;
                    } else if ("hashCode".equals(methodName)) {
                        continue;
                    } else if ("getClass".equals(methodName)) {
                        continue;
                    } else if ("notify".equals(methodName)) {
                        continue;
                    } else if ("notifyAll".equals(methodName)) {
                        continue;
                    }

                    Type[] type = method.getGenericParameterTypes();
                    List<Object> parmType = new ArrayList<Object>(1);
                    Map<String, Object> mapMethodParams = new HashMap<String, Object>(2);
                    for (Type ty : type) {
                        parmType.add(ty);
                        List<Map<String, Object>> listAddParams = new ArrayList<Map<String, Object>>(4);
                        //有封装
//                        System.out.println("ty:" + ty.toString());
                        String[] parms = ty.toString().split("class ");//对象参数
                        if (parms != null && parms.length > 1) {
                            //class com.dounine.japi.Entity
//                            String[] paramTypeCheckObject = parms[1].split("com");//实体前缀
                            String[] paramTypeCheckObject = parms[1].split(entityPrePath);//实体前缀
                            if (paramTypeCheckObject != null && paramTypeCheckObject.length > 1) {
//                                System.out.println("paramTypeCheckObject:" + paramTypeCheckObject[1]);
                                Field[] fields = Class.forName(entityPrePath + paramTypeCheckObject[1]).getDeclaredFields();
                                for (Field field : fields) {
                                    Map<String, Object> map = new HashMap<String, Object>(1);
                                    map.put("attributeName", field.getName());
//                                    map.put("attributeDetail", field.getAnnotation(AttrAnnotation.class).Description());
                                    listAddParams.add(map);
                                }
                                mapMethodParams.put("entityParamAttr", listAddParams);
                            }

                        }
                        //无封装  int
                    }
                    String classUrl="";
                    String methodUrl="";
                    mapMethodParams.put("class", s);
                    mapMethodParams.put("methodName", method.getName());
                    mapMethodParams.put("returnType", method.getReturnType().getName());
                    mapMethodParams.put("paramType", parmType);
//                    if(demo1.getAnnotation(ClassAnnotation.class) !=null){
//                        classUrl = demo1.getAnnotation(ClassAnnotation.class).className()+"/";
//                    }
//                    if(method.getAnnotation(MethodAnnotation.class) !=null){
//                        methodUrl = method.getAnnotation(MethodAnnotation.class).name();
//                    }
                    mapMethodParams.put("demo","http://localhost/"+classUrl+methodUrl);
                    for (int i = 0; i < getAnnoInfo.size(); i++) {
                        if (method.getName().equals(getAnnoInfo.get(i).get("methodName"))) {
                            mapMethodParams.put("methodName", getAnnoInfo.get(i).get("methodName"));
                            mapMethodParams.put("paramLimit", getAnnoInfo.get(i).get("paramLimit"));
                            mapMethodParams.put("return", getAnnoInfo.get(i).get("return"));
                            mapMethodParams.put("desc", getAnnoInfo.get(i).get("desc"));
                        }
                    }
                    classList.add(mapMethodParams);
                }
                System.out.println("秒:" + classList);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public List<Map<String, Object>> anno(String filePath, String classname) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        try {
            BufferedReader bis = new BufferedReader(new FileReader(filePath + "/" + classname+".java"));
            StringBuilder sb = new StringBuilder();
            List<String> lines = new ArrayList<String>();
            while (bis.read() != -1) {
                lines.add(bis.readLine());
            }
            for (String s : lines) {
                sb.append(s);
            }
            String context = sb.toString();
            Pattern leftpattern = Pattern.compile("/\\*{2}");
            Matcher leftmatcher = leftpattern.matcher(context);
            Pattern rightpattern = Pattern.compile("\\)\\p{Blank}*\\{");
            Matcher rightmatcher = rightpattern.matcher(context);

            List<String> list = new ArrayList<String>();
            String contexts = "";
            while (leftmatcher.find() && rightmatcher.find()) {
                while (rightmatcher.start() < leftmatcher.start()) {
                    rightmatcher.find();
                }
                contexts = context.substring(leftmatcher.start(), rightmatcher.start());
                list.add(contexts);
            }
            for (String explain : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                String [] classSplit = explain.split("class\\s*"+classname+"\\s*\\{");
                if(classSplit.length>=2){
                    explain =classSplit[1];
                }
                String[] splits = explain.split("public");
                if (splits != null && splits.length >= 2) {
                    String returnAndParams = splits[1];
                    String[] splitMethods = returnAndParams.split("\\(");
                    if (splitMethods != null) {
                        String returnMethod = "";
                        returnMethod = splitMethods[0];//String ld:返回值+方法名
                        String[] methodNames = returnMethod.split("\\s");
                        if (methodNames != null) {
                            String methodName = "";
                            methodName = methodNames[2];//ld:方法名
                            map.put("methodName", methodName);
                        }
                    }
                }

                //拿到注释
                if (map.get("methodName") == null) {
                    continue;
                }
                if (splits != null) {
                    String annotation = splits[0];
                    String[] sqr = annotation.split("\\*\\s*#");
                    for (String explainSplit : sqr) {
                        if (explainSplit.contains("/**")) {
                            String ss[] = explainSplit.split("/\\**");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                descs = ss[1].replace("    *","").trim();
                            } else {
                                descs = "";
                            }
                            if (map.get("desc") != null) {
                                map.put("desc", map.get("desc") + "," + descs);
                            } else {
                                map.put("desc", descs);
                            }
                        } else if (explainSplit.startsWith("demo")) {
                            String ss[] = explainSplit.split("demo");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                descs = ss[1];
                            } else {
                                descs = "";
                            }
                            if (map.get("demoUrl") != null) {
                                map.put("demoUrl", map.get("demoUrl") + "," + descs);
                            } else {
                                map.put("demoUrl", descs);
                            }
                        } else if (explainSplit.startsWith("plimit")) {
                            String ss[] = explainSplit.split("plimit");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                descs = ss[1];
                            } else {
                                descs = "";
                            }
                            if (map.get("paramLimit") != null) {
                                map.put("paramLimit", map.get("paramLimit") + "," + descs);
                            } else {
                                map.put("paramLimit", descs);
                            }
                        } else if (explainSplit.startsWith("return")) {
                            String ss[] = explainSplit.split("return");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                String[] returnStr = ss[1].split("\\**/");
                                if (returnStr != null && returnStr.length > 0) {
                                    descs = returnStr[0];
                                }
                            } else {
                                descs = "";
                            }
                            map.put("return", descs);
                        }
                    }
                }
                if (map.size() > 0) {
                    listMap.add(map);
                }
            }
            System.out.println("接货;" + listMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listMap;
    }
}
