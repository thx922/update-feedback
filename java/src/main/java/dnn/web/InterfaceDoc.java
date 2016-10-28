package dnn.web;

import dnn.common.MethodVersionAnnotation;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ike on 16-10-07.最新,前面是interfacedocnew
 */
@RestController
@RequestMapping("interfaceApiDoc")
public class InterfaceDoc {
    private ClassLoader classLoader;
    private List<Map<String, Object>> halist = null;
    private String classAn = null;//类描述
    private String packageInfo = null;
    private String packageNameInfo = null;
    private int pckIndex = -1;
    private int pckIndex1 = -1;
    private String contentNewFlag ="";
    private String guideNewFlag ="";
    private String entityPrePath = "dnn.web";
    private String filePath = "/home/ike/java/java/feedback/java/src/main/java/dnn/web";
    private String htmlFilePath = "/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc";

    @GetMapping(value = {""})
    public ModelAndView index( ) {
        System.out.println("fh");
        return new ModelAndView("redirect:/views/interfaceapidoc/web2guide.jsp");
    }

    @GetMapping("tpl/{act}")
    public ModelAndView index1(@PathVariable String act) {
        System.out.println(act.replace("-", "/"));
//        return new ModelAndView("interfaceapidoc/" + act.replace("-", "/"));
        return new ModelAndView(".././views/interfaceapidoc/" + act.replace("-", "/"));
    }

    @GetMapping("findInterface")
    public ModelAndView findInterfaceApiDoc() {
//        FirstMethod( filePath , entityPrePath , htmlFilePath);
//        return new ModelAndView("interfaceapidoc/dnn/guide");
        return new ModelAndView("../../views/interfaceapidoc/index");
    }

    public String format(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int i = 0,len = jsonStr.length(); i < len; i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "</span><br/>\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "</span><br/>\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("</span><br/>\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();

    }

    private String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("&nbsp;&nbsp;&nbsp;");
        }
        levelStr.replace(0,levelStr.length(),"<span>"+levelStr.toString());
        return levelStr.toString();
    }


    public String FirstMethod(String webProjectName ,String webFilePaths , String webPackageName , String htmlFilePaths , List<String> newFile ) {
        File file = new File(webFilePaths);
        String[] names = file.list();

        List<Object> listActName = new ArrayList<Object>();

        List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
        halist = new ArrayList<>();
        halist = dirToName(webFilePaths, webPackageName.trim(), names, null);
        for (Map<String, Object> maps : halist) {
            String filePaths = maps.get("filePath").toString();
            String entityPrePaths = maps.get("entityPrePath").toString();
            String classname = maps.get("classname").toString();
            String dir = null;
            if (maps.get("dir") != null) {
                dir = maps.get("dir").toString();
            }
            classList = webActName(filePaths, entityPrePaths, classname, dir);
            listActName.add(classList);
        }
        List<String> htls = htmlCreate(webProjectName ,webFilePaths ,webPackageName ,listActName ,htmlFilePaths ,newFile);
        StringBuffer sb= new StringBuffer("");
        for(String str :htls){
            sb.append(str);
            sb.append(",");
        }
        return sb.toString();
    }

    public List<Map<String, Object>> dirToName(String filePath, String entityPrePath, String[] names, String dir) {
        String excludeSelf = this.getClass().getName();
        excludeSelf = excludeSelf.substring(excludeSelf.lastIndexOf(".")+1,excludeSelf.length());
        String filePathName = "";
        for (String s : names) {
            Map<String, Object> map = new HashMap<>();
            String excludeSelfs = filePath.replaceAll("\\/",".") +"."+excludeSelf+".java";
            String pathNows = filePath.replaceAll("\\/",".")+"."+s;
            if(pathNows.equals(excludeSelfs)){
                continue;//不检测这个类interfacedoc
            }
            if (s.trim().equals("package-info.java")) {
                continue;
            }
            //直接是目录
            if (!s.contains(".java")) {
                filePathName = filePath + "/" + s;
                File fileDir = new File(filePathName);// /w
                if (dir != null) {
                    s = dir + "." + s;
                }
                if (fileDir.isDirectory()) {
                    String[] fileDirNames = fileDir.list();
                    for (int i = 0; i < fileDirNames.length; i++) {
                        dirToName(filePathName, entityPrePath, fileDirNames, s);
                        break;
                    }
                    continue;
                }
                continue;
            }
            map.put("filePath", filePath);
            map.put("entityPrePath", entityPrePath);
            map.put("classname", s);
            map.put("dir", dir);
            halist.add(map);
        }
        return halist;
    }

    public List<Map<String, Object>> anno(String filePath, String classname, Class<?> demo1) {
        classAn =null;
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        try {
            List<String> readDocList = readDocByStream(filePath, classname);
            for (String explain : readDocList) {
                Map<String, Object> map = new HashMap<String, Object>();
                String[] classSplit = explain.split("(class|interface)\\s*" + classname + "\\s*\\{");
                String classSplitLeft = null;
                if (classSplit.length >= 2) {
                    explain = classSplit[1];
                    classSplitLeft = classSplit[0];
                }
                if (StringUtils.isNotBlank(classSplitLeft)) {
                    classAn = classAnnotations(classSplitLeft, "#classDes");
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
//                System.out.println("测试方法名:"+map.get("methodName"));
                if (splits != null && splits.length > 0) {
                    String annotation = splits[0];
                    String[] sqr = annotation.split("\\*\\s*#");
                    for (String explainSplit : sqr) {
                        if (explainSplit.contains("/**")) {
                            String ss[] = explainSplit.split("/\\**");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                descs = ss[1].replace("    *", "").trim();
                            } else {
                                descs = "";
                            }
                            if (map.get("desc") != null) {
                                map.put("desc", map.get("desc") + ";" + descs);
                            } else {
                                map.put("desc", descs);
                            }
                        } else if (explainSplit.startsWith("nameDes")) {
                            map = annotationSplit(explainSplit, map, "nameDes", "nameDes");
                        } else if (explainSplit.startsWith("example")) {
                            map = annotationSplit(explainSplit, map, "example", "example");
                        } else if (explainSplit.startsWith("demo")) {
                            map = annotationSplit(explainSplit, map, "demo", "demoUrl");
                        } else if (explainSplit.startsWith("exclude")) {
                            map = annotationSplit(explainSplit, map, "exclude", "paramExclude");
                        } else if (explainSplit.startsWith("include")) {
                            map = annotationSplit(explainSplit, map, "include", "paramInclude");
                        } else if (explainSplit.startsWith("version")) {
                            map = annotationSplit(explainSplit, map, "version", "methodVersion");
                        } else if (explainSplit.startsWith("return")) {
                            map = annotationSplit(explainSplit, map, "return", "return");
                        }
                    }
                }
                if (map.size() > 0) {
                    listMap.add(map);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listMap;
    }

    public List<Map<String, Object>> webActName(String filePath, String entityPrePath, String s, String dir) {
        //直接是文件.java
        String[] strings = s.split(".java");
        Class<?> demo1 = null;
        List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();

        String packageName = "";
        try {
            if (StringUtils.isNotBlank(dir)) {
                demo1 = Class.forName(entityPrePath + "." + dir + "." + strings[0]);
                packageName = entityPrePath + "." + dir;
            } else {
                demo1 = Class.forName(entityPrePath + "." + strings[0]);
                packageName = entityPrePath;
            }

            List<String> readPackageInfoList = readPackageInfo(filePath, "package-info");
            packageInfo = packageSplitInfo(readPackageInfoList, "\\#packageInfo");
            if (packageInfo == null) {
                packageInfo = packageName + "包";
            }
            packageNameInfo = packageSplitInfo(readPackageInfoList, "\\#packageName");
            if (packageNameInfo == null) {
                packageNameInfo = packageName;
            }

            List<Map<String, Object>> getAnnoInfo = anno(filePath, strings[0], demo1);//解析注释
            Method[] dan = demo1.getMethods();//获取所有方法
            for (Method method : dan) {
                MethodVersionAnnotation mva = method.getAnnotation(MethodVersionAnnotation.class);
                String mVersionFromAnnotation = null;
                if (mva != null) {
                    mVersionFromAnnotation = mva.version();
                }
                String methodName = method.getName();
                List<String> removeSomeMethod = Arrays.asList("wait","equals","toString","hashCode","getClass","notify","notifyAll");
                Boolean flag =false;
                for(String str : removeSomeMethod){
                    if(methodName.equals(str)){
                        flag =true;
                        continue;
                    }
                }
                if(flag){
                    continue;
                }
                Map<String, Object> mapMethodParams = new HashMap<String, Object>(2);

                Type[] type = method.getGenericParameterTypes();
                List<Object> parmType = new ArrayList<Object>(1);
                for (Type ty : type) {
                    String pty = ty + "";
                    if (pty.contains("[")) {
                        int left = pty.indexOf("[");
                        pty = pty.substring(0, left) + pty.substring(left + 1);
                    }
                    parmType.add(pty);
//                        System.out.println("参数个数:"+pty);
                    mapMethodParams = paramTypeAndName(demo1, method, mapMethodParams);
                    mapMethodParams = paramEntityAttr( ty, mapMethodParams);
                }
                mapMethodParams.put("className", strings[0]);
                mapMethodParams.put("class", s);
                mapMethodParams.put("packageName", packageName);
                mapMethodParams.put("methodName", method.getName());
                mapMethodParams.put("returnType", method.getReturnType().getName());
                mapMethodParams.put("paramType", parmType);

                MethodRequsetDeal(demo1, method, mapMethodParams);
                mapMethodParams = mapMethodParamsAdd(method, mVersionFromAnnotation, getAnnoInfo, mapMethodParams);

                if (mapMethodParams.get("paramsValue") != null) {
                    if (mapMethodParams.get("paramInclude") != null && !mapMethodParams.get("paramInclude").equals("")) {
                        mapMethodParams = paramInclude(mapMethodParams, "paramInclude");
                    } else if ((mapMethodParams.get("paramInclude") == null || mapMethodParams.get("paramInclude").equals("")) && mapMethodParams.get("paramExclude") != null) {
                        mapMethodParams = paramInclude(mapMethodParams, "paramExclude");
                    } else {
                        if (mapMethodParams.get("paramsValue") != null) {
                            String paramsValue = mapMethodParams.get("paramsValue").toString().trim();
                            paramsValue = paramsValue.replaceFirst("\\[", "");
                            mapMethodParams.put("paramTypeList", "[" + paramsValue.substring(0, paramsValue.length() - 1) + "]");
                        }
                    }
                }
                if (classAn == null) {
                    String[] classNameSplit = mapMethodParams.get("class").toString().split(".java");
                    mapMethodParams.put("classDes", classNameSplit[0]);
                } else {
                    mapMethodParams.put("classDes", classAn);
                }
                mapMethodParams.put("packageInfo", packageInfo);
                mapMethodParams.put("packageNameInfo", packageNameInfo);
//                    mapMethodParams.remove("paramType");
//                    mapMethodParams.remove("paramsValue");
                classList.add(mapMethodParams);
            }
                System.out.println("秒:" + classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    public void MethodRequsetDeal(Class<?> demo1, Method method, Map<String, Object> mapMethodParams) {
        Annotation[] actAnnos = demo1.getDeclaredAnnotations();
        String[] rmVlues = null;
        for (Annotation annotation : actAnnos) {
            String annoSimpleName = annotation.annotationType().getSimpleName();
            if ("RequestMapping".equals(annoSimpleName)) {
                rmVlues = demo1.getDeclaredAnnotation(RequestMapping.class).value();
            }
        }
        Annotation[] actMethodAnnos = method.getDeclaredAnnotations();
        for (Annotation annotation : actMethodAnnos) {
            String annoSimpleName = annotation.annotationType().getSimpleName();
            demoUrl(method, annoSimpleName, rmVlues, mapMethodParams);
        }
    }

    public void demoUrl(Method method, String reqMethod, String[] rmVlues, Map<String, Object> mapMethodParams) {
       try {
           String[] methodValues = null;
           RequestMethod[] reqMethods = null;
           String[] reqParams = null;
           String reqStr = "";

           switch (reqMethod) {
               case "RequestMapping":
                   methodValues = method.getDeclaredAnnotation(RequestMapping.class).value();
                   reqMethods = method.getDeclaredAnnotation(RequestMapping.class).method();
                   reqParams = method.getDeclaredAnnotation(RequestMapping.class).params();
                   reqStr = "REQUEST";
                   break;
               case "PostMapping":
                   methodValues = method.getDeclaredAnnotation(PostMapping.class).value();
                   reqParams = method.getDeclaredAnnotation(PostMapping.class).params();
                   reqStr = "POST";
                   break;
               case "GetMapping":
                   methodValues = method.getDeclaredAnnotation(GetMapping.class).value();
                   reqParams = method.getDeclaredAnnotation(GetMapping.class).params();
                   reqStr = "GET";
                   break;
               case "PutMapping":
                   methodValues = method.getDeclaredAnnotation(PutMapping.class).value();
                   reqParams = method.getDeclaredAnnotation(PutMapping.class).params();
                   reqStr = "PUT";
                   break;
               case "PatchMapping":
                   methodValues = method.getDeclaredAnnotation(PatchMapping.class).value();
                   reqParams = method.getDeclaredAnnotation(PatchMapping.class).params();
                   reqStr = "PATCH";
                   break;
               case "DeleteMapping":
                   methodValues = method.getDeclaredAnnotation(DeleteMapping.class).value();
                   reqParams = method.getDeclaredAnnotation(DeleteMapping.class).params();
                   reqStr = "DELETE";
                   break;
           }

           List<String> annoMap = new ArrayList<>();
           if (methodValues != null) {
               for (int i = 0; i < methodValues.length; i++) {
                   String urlStr = "http://localhost:8080";
                   if (reqStr.equals("REQUEST") && reqMethods != null && reqMethods.length > 0) {
                       urlStr = reqMethods[0] + " -> " + urlStr;
                   }
                   if (!reqStr.equals("REQUEST")) {
                       urlStr = reqStr + " -> " + urlStr;
                   }
                   if (rmVlues != null && rmVlues.length > 0) {
                       urlStr = urlStr + "/" + rmVlues[0] + "/";
                   } else {
                       urlStr = urlStr + "/";
                   }
                   urlStr = urlStr + methodValues[i].replaceAll("\\/", "");
                   if (reqParams != null && reqParams.length > 0) {
                       urlStr = urlStr + " & " + reqParams[i];
                   }
                   annoMap.add(urlStr);
               }
               mapMethodParams.put("demo", annoMap);
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }


    public Map<String, Object> paramTypeAndName(Class<?> demo1, Method method, Map<String, Object> mapMethodParams) throws Exception {
//        CtMethod cm = cc.getDeclaredMethod(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] parameterType = method.getGenericParameterTypes();
        String[] paramTypeNames = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
//            paramTypeNames[i] = parameterTypes[i].getName();
            paramTypeNames[i] = parameterTypes[i].getTypeName();
        }

        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(demo1);
        pool.insertClassPath(classPath);
        CtClass cc = pool.get(demo1.getName());
        CtMethod cm = cc.getDeclaredMethod(method.getName(), pool.get(paramTypeNames));

        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute != null) {
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            String[] paramsValue = new String[cm.getParameterTypes().length];
            List<Object> paramsValueStr = new ArrayList<Object>();
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramsValue.length; i++) {
//                paramsValue[i] = attr.variableName(i + pos);
                paramsValue[i] = parameterType[i].getTypeName();
//                String typeName =parameterTypes[i].getTypeName();
                String typeName = parameterType[i].getTypeName();
                List<String> typePre = Arrays.asList("java.util.", "java.lang.", "java.io.",
                        "javax.servlet.http.");
                for (String str : typePre) {
                    if (typeName.contains(str)) {
                        typeName = typeName.replaceAll(str, "");
                        continue;
                    }
                }
                String typeNameStr[] = typeName.split("\\.");
                if (typeNameStr != null && typeNameStr.length > 0) {
                    typeName = typeNameStr[typeNameStr.length - 1];
                }
                if (typeName.equals("HttpServletRequest") || typeName.equals("HttpServletResponse")) {
                    continue;
                }
                String parameterTypesAndName = typeName + " " + attr.variableName(i + pos);
                paramsValueStr.add(parameterTypesAndName.replaceAll(",", " "));
            }
            mapMethodParams.put("paramsValue", paramsValueStr);
        }
        return mapMethodParams;
    }

    public Map<String, Object> paramEntityAttr( Type ty, Map<String, Object> mapMethodParams) throws Exception {
        List<String> listAddParams = new ArrayList<String>();
        //有封装
        String[] parms = ty.toString().split("class ");//对象参数
        if (parms != null && parms.length > 1) {
            //class com.dounine.japi.Entity
            if (!parms[1].startsWith("java")) {//非java类实体,是用户自定义的
                Field[] fields = Class.forName(parms[1]).getDeclaredFields();
                int fieldsIndex = parms[1].lastIndexOf(".");
                String fieldsIndexStr = parms[1].substring(fieldsIndex + 1, parms[1].length());
                for (Field field : fields) {
                    Annotation[] annos = field.getAnnotations();
                    List<Object> listAttr = new ArrayList<>();
                    for (Annotation annotation : annos) {
//                        System.out.println("复:"+annotation);
//                        System.out.println("复合物:"+annotation.annotationType());
                        String annotationType =annotation.annotationType().getCanonicalName();
//                        System.out.println("物:" + annotation.annotationType().getCanonicalName());
                        listAttr.add(annotation);
                    }

                    //字段深入判断
                    String oo =objectFieldFromObject(field, parms[1]);
                    if(listAttr.size()>0){
                        listAddParams.add(oo+"<div><i>约束:"+listAttr+"</i></div>");
                    }else{
                        listAddParams.add(oo+"<br/>");
                    }
                }
                String sd = listAddParams.toString().substring(1,listAddParams.toString().length()-1);
                mapMethodParams.put(fieldsIndexStr + "ObjectEntityParamAttr", sd.replaceAll("\\,\\s*\\<b\\>","<b>"));
            }

        }
        //无封装  int
        return mapMethodParams;
    }

    public String objectFieldFromObject( Field field, String typeName){
        StringBuffer recursionField = new StringBuffer("");
        Class clz =field.getType();
        String fieldSelf = clz.getTypeName();
        if(clz != null && clz.getClassLoader() == null && !fieldSelf.equals(typeName)){//java对象
            String fielStr =  field.getGenericType().getTypeName();
            List<String> typePre = Arrays.asList("java.util.", "java.lang.", "java.io.","java.time");
            for (String str : typePre) {
                if (fielStr.contains(str)) {
                    fielStr = fielStr.replaceAll(str, "");
                    continue;
                }
            }
            recursionField.append("<b>"+field.getName()+" : </b>"+fielStr.replaceAll("\\<","&lt;").replaceAll("\\>","&gt;")+"  ,");
        }else if((clz == null || clz.getClassLoader() != null)&& fieldSelf.equals(typeName) && !field.getType().isEnum()){
            recursionField.append("<b>"+field.getName()+" : </b> $ ,");
        }else{                                          //非java对象
                Field[] fds =clz.getDeclaredFields();
                for(Field fd: fds){
                    String fielStr =  fd.getGenericType().getTypeName();
                    int index = fielStr.lastIndexOf(".");
                    if( !clz.isEnum() ){
                        if(fd.getType() == null || fd.getType().getClassLoader() != null){
                            recursionField.append(objectFieldFromObject(  fd, fd.getGenericType().getTypeName()));
                        }else {
                            recursionField.append("<p>" + fd.getName() + " :  "+fielStr.substring(index+1,fielStr.length())+ " ,</p>");
                        }
                    } else if( clz.isEnum() ){
                        if( fd.getType().isEnum()){
                            recursionField.append("<p>"+fd.getName()+" :   '"+field.getName()+"."+fd.getName()+"' ,</p>");
                        }
                    }
            }
            recursionField.replace(0,recursionField.length(),"<span style='margin-left:60px;display:inline-block'>"+recursionField.toString()+"</span>");
            recursionField.append("<br/>} ,");
            recursionField.replace(0,recursionField.length(),"<b>"+field.getName()+" : </b>{<br/>"+recursionField.toString());

        }
        return recursionField.toString();
    }

    public Map<String, Object> paramInclude(Map<String, Object> mapMethodParams, String paramsInclude) {
        String paramInclude = mapMethodParams.get(paramsInclude).toString().trim();
        String[] paramIncludeArrs = paramInclude.split(" ");
        String paramsValue = mapMethodParams.get("paramsValue").toString().trim();
        String[] paramsValueAs1 = paramsValue.split("\\[");
        String[] paramsValueAs2 = paramsValueAs1[1].split("\\]");
        if (paramsValueAs2 == null || paramsValueAs2.length <= 0) {
            mapMethodParams.put("paramTypeList", null);
            return mapMethodParams;
        }
        String[] paramsValueArrs = paramsValueAs2[0].split(",");   //逗号分割参数名
        List<Object> paramTypeList = new ArrayList<Object>();
        for (int k = 0; k < paramsValueArrs.length; k++) {
            boolean flag = true;
            String temp = null;
            String temps = paramsValueArrs[k];
            String[] tempSplits = temps.split("\\s+");
            String tempSplit = null;
            if (tempSplits != null && tempSplits.length > 2) {
                tempSplit = tempSplits[tempSplits.length - 1];
            } else if (tempSplits != null && tempSplits.length == 2) {
                tempSplit = tempSplits[1];
            }

            String[] tSplits = null;
            String tSplit = null;
            if (tempSplit.contains("]")) {
                tSplits = tempSplit.split("]");
                tSplit = tSplits[0];
            }
            if (tSplit == null) {
                temp = tempSplit;
            } else {
                temp = tSplit;
            }
            for (int j = 0; j < paramIncludeArrs.length; j++) {
                if (paramIncludeArrs[j].equals("*")) {
                    paramTypeList.clear();
                    if (paramsInclude.equals("paramInclude")) {
                        paramTypeList.add(paramsValue.replaceAll("\\[", "").replaceAll("\\]", ""));
                    }
                    break;
                }
                if (!temp.equals(paramIncludeArrs[j])) {
                    flag = false;
                } else {
                    flag = true;
                }
                if (!flag) {
                    paramTypeList.add(temps);
                }
            }
        }
        Object paramTypeListToObject = paramTypeList;
        mapMethodParams.put("paramTypeList", paramTypeListToObject);
        return mapMethodParams;
    }

    public Map<String, Object> mapMethodParamsAdd(Method method, String mVersionFromAnnotation, List<Map<String, Object>> getAnnoInfo, Map<String, Object> mapMethodParams) {
        for (int i = 0; i < getAnnoInfo.size(); i++) {
            if (mVersionFromAnnotation != null) {
                if (method.getName().equals(getAnnoInfo.get(i).get("methodName")) && mVersionFromAnnotation.equals(getAnnoInfo.get(i).get("methodVersion"))) {
                    mapMethodParams.put("methodName", getAnnoInfo.get(i).get("methodName"));
                    mapMethodParams.put("paramExclude", getAnnoInfo.get(i).get("paramExclude"));
                    mapMethodParams.put("paramInclude", getAnnoInfo.get(i).get("paramInclude"));
                    mapMethodParams.put("return", getAnnoInfo.get(i).get("return"));
                    mapMethodParams.put("desc", getAnnoInfo.get(i).get("desc"));
                    mapMethodParams.put("methodVersion", getAnnoInfo.get(i).get("methodVersion"));
                    mapMethodParams.put("nameDes", getAnnoInfo.get(i).get("nameDes"));
                    mapMethodParams.put("example", getAnnoInfo.get(i).get("example"));
                }
            }

        }
        return mapMethodParams;
    }

    public Map<String, Object> annotationSplit(String explainSplit, Map<String, Object> map, String StrSplit, String mapStr) {
        String ss[] = explainSplit.split(StrSplit);
        String descs = "";
        if (ss != null && ss.length >= 2) {
            String[] returnStr = ss[1].split("\\*+/");
            if (returnStr != null && returnStr.length > 0) {
                descs = returnStr[0].trim();
            }
        } else {
            descs = "";
        }
        if (map.get(mapStr) != null) {
            map.put(mapStr, map.get(mapStr) + " " + descs);
        } else {
            map.put(mapStr, descs);
        }
        return map;
    }

    public List<String> readDocByStream(String filePath, String classname) throws FileNotFoundException, IOException {
        BufferedReader bis = new BufferedReader(new FileReader(filePath + "/" + classname + ".java"));
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
//            Pattern rightpattern = Pattern.compile("\\*/[\\s\\S]*public[\\s\\S]*\\)\\p{Blank}*[\\s\\S]*\\{");
        Pattern rightpattern = Pattern.compile("\\;");
        Matcher rightmatcher = rightpattern.matcher(context);

        Pattern rightpattern1 = Pattern.compile("\\*/");
        Matcher rightmatcher1 = rightpattern1.matcher(context);

        List<String> list = new ArrayList<String>();
        String contexts = "";

        while (leftmatcher.find() && rightmatcher.find() && rightmatcher1.find()) {
            while (rightmatcher.start() < leftmatcher.start()) {
                rightmatcher.find();
            }
            while (rightmatcher.start() < rightmatcher1.start()) { // /** ; */;
                rightmatcher.find();
            }
            contexts = context.substring(leftmatcher.start(), rightmatcher.start());
            list.add(contexts);
        }
        return list;
    }

    public String classAnnotations(String classSplitLeft, String splitStr) {
        String[] classAnnos = null;
        String[] classDesc = null;
        String str = null;
//        classAnnos =classSplitLeft.split("#classDes");
        classAnnos = classSplitLeft.split(splitStr);
        if (classAnnos != null && classAnnos.length >= 2) {
            str = classAnnos[1];
            classDesc = str.split("\\*+");
            str = classDesc[0];
        }
        return str;

    }

    public List<String> readPackageInfo(String filePath, String classname) throws IOException {
        FileReader fileReader = null;
        List<String> list = new ArrayList<String>();
        try {
            fileReader = new FileReader(filePath + "/" + classname + ".java");

        } catch (FileNotFoundException e) {
            System.out.println("该包没写package-info描述:" + e.getMessage());
        }
        if (fileReader == null) {
            return null;
        }
        BufferedReader bis = new BufferedReader(fileReader);

        StringBuilder sb = new StringBuilder();
        List<String> lines = new ArrayList<String>();
        while (bis.read() != -1) {
            lines.add(bis.readLine());
        }
        for (String s : lines) {
            sb.append(s);
        }
        String context = sb.toString();
        Pattern leftpattern = Pattern.compile("\\*{2}");
        Matcher leftmatcher = leftpattern.matcher(context);
        Pattern rightpattern = Pattern.compile("\\**/");
        Matcher rightmatcher = rightpattern.matcher(context);
        String contexts = "";
        while (leftmatcher.find() && rightmatcher.find()) {
            while (rightmatcher.start() < leftmatcher.start()) {
                rightmatcher.find();
            }
            contexts = context.substring(leftmatcher.start(), rightmatcher.start());
            list.add(contexts);
        }
        return list;
    }

    public String packageSplitInfo(List<String> list, String splitStr) {
        if (list != null && list.size() > 0) {
            String[] packages = list.get(0).split(splitStr);
            if (packages != null && packages.length > 1) {
                packages = packages[1].split("\\*+");
                return packages[0].toString();
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public List<String> htmlCreate(String webProjectName ,String webFilePaths ,String webPackageName , List<Object> listActName ,String htmlFilePaths ,List<String> newfile) {
        List<String> htmlsFile = new ArrayList<>();
        guideNewFlag ="";
        pckIndex = -1;
        pckIndex1 = -1;

        String pckPath = "";
        List<String> list = new ArrayList<>();
        if (listActName == null && listActName.size() <= 0) {
            list = null;
            return list;
        }
        Map<String, Object> pckNameMap = new HashMap<>();

        Map<String, Object> pckNameIndexMap = new HashMap<>();
        Map<Object, Object> pckNameIndexMap1 = new HashMap<>();
        Map<String, Object> pckDescIndexMap = new HashMap<>();
        Map<String, Object> pckNameDescMap = new HashMap<>();
        String contentsIndex = " <%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%><!DOCTYPE html><html lang='en'><head>";
        StringBuffer sbIndex = new StringBuffer(contentsIndex)
                .append("<meta charset='UTF-8'> <title>使用指南</title> <link rel='stylesheet' href='${ctx}/html/css/guide_red.css' my-color='#E54A5C' title='theme_red'> <link rel='stylesheet' href='${ctx}/html/css/guide_blue.css' my-color='#238DFA' title='theme_blue' disabled='disabled'> <link rel='stylesheet' href='${ctx}/html/css/guide_green.css' my-color='#0BC8E1' title='theme_green' disabled='disabled'> <link rel='stylesheet' href='${ctx}/html/css/guide_yellow.css' my-color='#FFCC5E' title='theme_yellow' disabled='disabled'><script src='${ctx}/html/js/jquery.min.js'></script><script src='${ctx}/html/js/jquery.cookie.js'></script><script src='${ctx}/html/js/guide.js'></script><script src='/html/js/router.js'></script></head>")
                .append("<body>")
                .append("<header><div class='logo'><a href='${ctx}/views/interfaceapidoc/index.jsp'><img src='${ctx}/html/img/logo.png' ></a></div><div class='changeColor'><a href='javascript:void(0)' id='theme_blue' style='background:#238DFA' ></a><a href='javascript:void(0)' id='theme_yellow' style='background:#FBE786'></a><a href='javascript:void(0)' id='theme_green' style='background:#22CB56'></a><a href='javascript:void(0)' id='theme_red' style='background:#F65866;display:none' ></a></div>")
                .append("<div class='search'><input type='text' id='search'><button class='searchBtn'>搜索</button><div class='searchtxt'></div></div>")
                .append("</header>")
                .append("<div class=\"conTitle\"></div>")
                .append("<nav ><span hidden id='spanId'>"+webProjectName+"</span>");

        for (int i = 0; i < listActName.size(); i++) {
            List<Map<String, Object>> classList = new ArrayList<>();
            classList = (List<Map<String, Object>>) listActName.get(i);
            String pckName = classList.get(0).get("packageName").toString();
            int pckValue = 0;
            if (pckNameMap.get(pckName) != null) {
                String pckNameValue = pckNameMap.get(pckName).toString();
                int index = pckNameValue.indexOf("-");
                pckValue = Integer.parseInt(pckNameValue.substring(index + 1, pckNameValue.length()).trim()) + 1;
                pckNameValue = pckNameValue.substring(0, index);
                pckNameMap.put(pckName, pckNameValue + "-" + pckValue);
                if (pckIndex > Integer.parseInt(pckNameValue.trim())) {
                    pckIndex1 = pckIndex;
                    pckIndex = Integer.parseInt(pckNameValue.trim());
                } else {
                    pckIndex = pckIndex;
                }
            } else {
                if (pckIndex1 > pckIndex) {
                    pckIndex = pckIndex1;
                }
                pckIndex = pckIndex + 1;
                pckNameMap.put(pckName, pckIndex + "-" + pckValue);
                pckNameIndexMap.put(pckName, "");
                pckNameIndexMap1.put(pckIndex, pckName);
                pckDescIndexMap.put(pckName, "");
                pckNameDescMap.put(pckName, "");
            }

            String strClassName = "";
            String strClassNameLast = "";
            StringBuffer liIndex = new StringBuffer("");
            String fileName = "";
            for (Map<String, Object> maps : classList) {
                fileName = maps.get("className").toString();
            }

            contentNewFlag ="";
            String jspPath  = htmlFilePaths+"/"+webProjectName+"/"+classList.get(0).get("packageName").toString().replaceAll("\\.", "/")+"/"+fileName+".jsp";
            if( newfile != null && newfile.size()>0 ){
                for(String item :newfile){
                    if( item.trim().equals(jspPath.trim())){
                        contentNewFlag ="新图标";
                        guideNewFlag = "新图标";
                        break;
                    }
                }
            }

            String pckDescIndexMapContent = "";
            String pckNameDescMapContent = "";
            int pckIndexInt = pckIndex + 1;
            int pckValueInt = pckValue + 1;
            String fileContents = "";
            StringBuffer sb = new StringBuffer(fileContents)
                    .append("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>")
                    .append("<div id='" + pckIndexInt + "'> <div id='" + pckIndexInt + "-" + pckValueInt + "'>");
            for (int j = 0; j < classList.size(); j++) {
                Map<String, Object> map = new HashMap<>();
                map = classList.get(j);

                String dData = dealCurlExampleData(map);
                String[] examples = exampleValueHtml( map , dData);

                String methodDes = "no Description";
                if (map.get("desc") != null) {
                    methodDes = map.get("desc").toString();
                }
                String methodVer = "No Version Write";
                if (map.get("methodVersion") != null && "" != map.get("methodVersion")) {
                    methodVer = map.get("methodVersion").toString();
                }
                String methodName = "";
                if (map.get("nameDes") != null && map.get("nameDes") != "") {
                    methodName = map.get("nameDes").toString();
                } else {
                    methodName = map.get("methodName").toString();
                }
                String returnMessage = "无";
                if (map.get("return") != null && map.get("return") != "") {
                    returnMessage = format(map.get("return").toString());
                }
                int thirdIndex = j + 1;
                sb.append("<div div class='content' id='" + pckIndexInt + "-" + pckValueInt + "-" + thirdIndex + "'>")
                        .append("<div class='main'>")
                        .append("<div class='useGuide'>")
                        .append("<h5>" + methodName + "</h5>")
                        .append("<p class='explain'></br>" + methodDes + "</p>")
                        .append("</div>")
                        .append("<div class='main_body'><div class='Interface'><h6>接口例子</h6>" +
                                "<div class='method'>" +
                                "<p><span>令牌</span>需要(未做)</p>" +
                                "<p><span>版本号</span>" + methodVer + "</p>" +
                                "<p><span>请求方式</span>" + examples[0] + "</p>" +
                                "</div>" +
                                "<div class='command'>" +
                                "" + examples[1] + "" +
                                "</div>" +
                                "响应报文 :<div class='command'>" +
                                ""+returnMessage+"" +
                                "</div></div><br/>")
                        .append("<div class='parameter'>")
                        .append("<h6>参数</h6>");

                if (map.get("paramTypeList") != null && !"".equals(map.get("paramTypeList")) && !"[]".equals(map.get("paramTypeList"))) {
                    System.out.println(map.get("paramTypeList"));
                    sb.append("<table  cellspacing='0'><thead><tr><th>参数类型</th><th>参数名</th><th>参数约束说明</th></tr></thead><tbody>");
                    int first = map.get("paramTypeList").toString().indexOf("[");
                    int last = map.get("paramTypeList").toString().lastIndexOf("]");
                    String str = map.get("paramTypeList").toString().substring(first + 1, last);
                    String[] params = str.split(",");
                    for (String param : params) {
                        int paramSplit = param.lastIndexOf(" ");
                        String ptype = "";
                        String pname = "";
                        if (paramSplit == -1) {
                            ptype = param;
                            pname = param;
                        } else {
                            ptype = param.substring(0, paramSplit);
                            ptype = ptype.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
                            pname = param.substring(paramSplit + 1, param.length());
                        }
                        String attrLists = "";
                        System.out.println("的成功分一二五日:" + map.get(ptype + "ObjectEntityParamAttr"));
                        if (map.get(ptype.trim() + "ObjectEntityParamAttr") != null) {
                            attrLists = map.get(ptype.trim() + "ObjectEntityParamAttr").toString();
                        }
                        sb.append("<tr ><td>" + ptype + "</td><td>" + pname + "</td><td>" + attrLists + "</td></tr>");

                    }
                    sb.append("</tbody>")
                            .append("</table>");
                } else {
                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No parameters.");
                }

                sb.append("</div></div>")
                        .append("</div></div>");

                String pckPathAttr =webProjectName+"-"+ map.get("packageName").toString().replaceAll("\\.", "-");
                Set set = pckNameIndexMap.entrySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry1 = (Map.Entry<String, String>) iterator.next();
                    if (entry1.getKey().equals(map.get("packageName").toString())) {
                        strClassName = "<div class='menu'> " +
                                "<a href='javascript:void(0)' class='a_width menuClick' my-attr = '" + pckPathAttr + "' id='" + fileName + "' title='" + map.get("classDes") + "'>";
                        if (StringUtils.isNotBlank(contentNewFlag)) {
                            strClassName = strClassName + "<span>" + map.get("classDes") + "<strong class='point'></strong></span><i class='iconfont'>&#xe608;</i></a>";
                        } else {
                            strClassName = strClassName + "<span>" + map.get("classDes") + "</span><i class='iconfont'>&#xe608;</i></a>";
                        }
                        strClassName = strClassName + "<ul class=' change'>";
                        if (map.get("nameDes") != null && map.get("nameDes") != "") {
                            liIndex.append("<li><a href='javascript:void(0)'  class='submenu' id='nav" + (int) (pckIndexInt - 1) + "-" + (pckValueInt - 1) + "-" + (thirdIndex - 1) + "'>" + map.get("nameDes") + "</a></li>");
                        } else {
                            liIndex.append("<li><a href='javascript:void(0)'  class='submenu' id='nav" + (int) (pckIndexInt - 1) + "-" + (pckValueInt - 1) + "-" + (thirdIndex - 1) + "'>" + map.get("methodName") + "</a></li>");
                        }
                        strClassNameLast = "</ul></div>";
                    }
                }
                pckDescIndexMapContent = map.get("packageInfo").toString();
                pckNameDescMapContent = map.get("packageNameInfo").toString();
            }
            String preMap = pckNameIndexMap.get(pckName).toString();
            String mapValue = preMap + strClassName + liIndex.toString() + strClassNameLast;
            pckNameIndexMap.put(pckName, mapValue);
            pckDescIndexMap.put(pckName, pckDescIndexMapContent);//用于pcktitle
            pckNameDescMap.put(pckName, pckNameDescMapContent);


            sb.append("</div>")
                    .append("</div>");
            fileContents = sb.toString();
            pckPath = classList.get(0).get("packageName").toString().replaceAll("\\.", "/");

            String paths = mkHtmlDir(webProjectName ,htmlFilePaths, pckPath, fileName);
            htmls(paths, fileContents);//生成子内容xx.html
            htmlsFile.add(paths+".jsp");
            htmlTxtFile(webFilePaths, webPackageName, paths);
        }

        for (int i = 0; i < pckNameIndexMap1.size(); i++) {//生成guide.jsp
            String guideIndexStr = pckNameIndexMap1.get(i).toString();
            String ss = pckNameIndexMap.get(guideIndexStr).toString();
            String pckDes = pckNameDescMap.get(guideIndexStr).toString();
            if (pckDes == null || pckDes == "") {
                pckDes = guideIndexStr;
            }

            String subnavStr = "<div class='subnav'>" + ss + "</div>";
            String mainbavStr = "";
//            if (StringUtils.isNotBlank(guideNewFlag)) {
            if( ss.contains("<strong class='point'></strong>")){
                mainbavStr = " <div class='mainbav'><a href='javascript:void(0)' class='a_width mainbavClick ' title='" + pckDescIndexMap.get(guideIndexStr) + "' ><span>" + pckDes + "<strong class='new'></strong></span><i class='iconfont'>&#xe608;</i></a>";
            } else {
                mainbavStr = " <div class='mainbav'><a href='javascript:void(0)' class='a_width mainbavClick ' title='" + pckDescIndexMap.get(guideIndexStr) + "' ><span>" + pckDes + "</span><i class='iconfont'>&#xe608;</i></a>";
            }
            String mainbavLastStr = "</div>";

            String mainbavStrs = mainbavStr + subnavStr + mainbavLastStr;
            sbIndex.append(mainbavStrs);
        }

        sbIndex.append("</nav>")
                .append("<div class='container '></div>")
                .append("</body></html>");
        String[] pckPaths = pckPath.split("/");
//        htmls(htmlFilePaths + "/" + pckPaths[0] + "/guide", sbIndex.toString());
//        htmls(htmlFilePaths + "/" + pckPaths[0] + "/index", indexHtml());
//        htmlsFile.add(htmlFilePaths + "/" + pckPaths[0] + "/guide.jsp");

        htmls(htmlFilePaths + "/" + webProjectName + "guide", sbIndex.toString());
        htmlsFile.add(htmlFilePaths + "/" + "/"+pckPaths[1]+ "guide.jsp");
        return htmlsFile;
    }

    public String mkHtmlDir(String webProjectName ,String filePath, String pckPath, String fileName) {
        String[] pckPaths = pckPath.split("/");
        String contextPath = "";
        File filePathFile = new File(filePath);
        if (!filePathFile.isDirectory()) {
            filePathFile.mkdir();
        }
        if( StringUtils.isNotBlank(webProjectName )){//把项目名加上去
            filePath = filePath+"/"+webProjectName;
            filePathFile = new File(filePath);
            if (!filePathFile.isDirectory()) {
                filePathFile.mkdir();
            }
        }
        for (String pckPathStr : pckPaths) {
            contextPath = contextPath + "/" + pckPathStr;
            String filePathContent = filePath.trim() + "/" + contextPath.trim();
            File f = new File(filePathContent);
            if (!f.isDirectory()) {
                f.mkdir();
            }
        }
        
        String paths = filePath + "/" + pckPath + "/" + fileName;
        return paths;
    }

//    filePath.setFileList("/home/ike/java/java/feedback/java/src/main/java/dnn/web1");
    //        filePath.setEntityList("dnn.web1");
//        filePath.setClientHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc/dnn/web1/admin/..");
    public void htmlTxtFile(String webFilePaths ,String webPackageName ,String jspPaths) {
        String fileame = ".txt";
        fileame = jspPaths + fileame;//生成的html文件保存路径。

        File f = new File(fileame);
        if( !f.exists() ){
            FileOutputStream fileoutputstream = null;// 建立文件输出流
            try {
//                String filePackagetoPath = webPackageName.replace(".", "/");
//                String[] webFile = jspPaths.split(filePackagetoPath);
                fileoutputstream = new FileOutputStream(fileame);
                String content = getFileMD5(new File(jspPaths + ".jsp"));
                content =" " +content+"-date:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
                byte tag_bytes[] = content.getBytes();
                fileoutputstream.write(tag_bytes);
                fileoutputstream.close();//关闭输出流
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ///home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc/dnn/web1/custom/procing/ProcingAct1.txt
        ///home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc/custom/procing/ProcingAct.txt
        ///home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc/dnn/web1/custom/procing/ProcingAct1.txt
        ///home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc/dnn/web1/custom/procing/ProcingAct1.jsp
    }

    public  String getFileMD5(File file) {
        if (file.isDirectory()){
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


    public void htmls(String paths, String fileContents) {
        String fileame = ".jsp";
        fileame = paths + fileame;//生成的html文件保存路径。
        FileOutputStream fileoutputstream = null;// 建立文件输出流
        try {
            fileoutputstream = new FileOutputStream(fileame);
            byte tag_bytes[] = fileContents.getBytes();
            fileoutputstream.write(tag_bytes);
            fileoutputstream.close();//关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> curlExampleData(String data){ //data : {username:'1243',password:'123'}
        List<String> arr = new ArrayList<>();
        System.out.println(data);
        int lastBraceIndex = data.lastIndexOf("}");
        data = data.replaceFirst("\\{", "").substring(0,lastBraceIndex-1);
        String[] dataList = data.split(",");
        System.out.println(data);
        for (String dataStr : dataList) {//username:'1243'
            if (StringUtils.isNotEmpty(dataStr) && StringUtils.isNotBlank(dataStr)) {
                int index = dataStr.lastIndexOf(":");
                String dataStrRight = dataStr.substring(index + 1, dataStr.length());
                if (dataStrRight.contains(".")) {//文件
                    dataStrRight = "=@" + dataStrRight.replaceAll("'", "");
                    dataStr = dataStr.substring(0,index)+dataStrRight;
                }
                arr.add(dataStr.replaceAll(":", "=").replaceAll("'", ""));
            }
        }
        return arr;
    }

    public String dealCurlExampleData(Map<String,Object> map){
        //获取example数据
        String dData ="";
        if(map.get("example") !=null  ){
            if( StringUtils.isNotBlank( map.get("example").toString() )){
                List<String>  curlExampleDataList = curlExampleData(map.get("example").toString());
                for(String cdata : curlExampleDataList){
                    if(!cdata.contains("=@")){
                        if(StringUtils.isNotBlank(dData)){
                            dData = dData.replaceFirst("'","");
                            dData = dData.replace(dData,"'"+cdata+"&"+dData);//多个参数用 &符号连接起来
                        }else{
                            dData = "'"+cdata+"'";
                        }
                    }
                    if(cdata.contains("=@")){
                        if(StringUtils.isNotBlank(dData)){
                            dData = dData +" -F '"+cdata+"'";//文件上传: -F 'f=XX.txtx'
                        }else{
                            dData = " -F '"+cdata+"'";
                        }
                    }
                }
                dData = dData.replace(dData,dData);
            }
        }
        return dData;
    }

    public String[] exampleValueHtml(Map<String,Object> map ,String dData){ //拼接页面中的curl
        String[] strings = new String[3];
        String crulType = "";
        StringBuffer crulData = new StringBuffer("");
        if (map.get("demo") != "" && map.get("demo") != null) {
            String[] cruls = map.get("demo").toString().split("\\,");
            for (String cls : cruls) {
                int crulTypeIndex = cls.indexOf("->");
                if (crulTypeIndex != -1) {
                    crulType = cls.substring(0, crulTypeIndex).replace("[", "");
                    String httpUrl = cls.substring(crulTypeIndex + 2, cls.length()).replace("]", "");
                    String dDatas ="请自己在这里拼参数";
                    if (StringUtils.isNotBlank(dData) || StringUtils.isNotEmpty(dData)) {
                        dDatas = dData.substring(1, dData.length() - 1);
                    }
                    if (crulType.trim().equals("POST")) { //post请求参数拼接在前面
                        dDatas = (StringUtils.isNotBlank(dData) || StringUtils.isNotEmpty(dData)) ? dData : "请自己在这里拼参数";
                        crulData.append("<div class='com_con'> curl -X " + crulType.trim() + "  --cookie 'token=b42a5e5845c046c49eec3d01c63365c0.2130706433.1476337615937'" + " -d " + dDatas + "&nbsp;'" + httpUrl.trim() + "'</div>");
                    } else if (crulType.trim().equals("GET") || crulType.trim().equals("PUT") || crulType.trim().equals("DELETE")) {
                        crulData.append("<div class='com_con'> curl -X " + crulType.trim() + "  --cookie 'token=b42a5e5845c046c49eec3d01c63365c0.2130706433.1476337615937'" + " '" + httpUrl.trim() + "?" + dDatas + "'</div>");
                    } else if (crulType.trim().equals("HEAD")) {
                        crulData.append("<div class='com_con'> curl -i -X " + crulType.trim() + "  --cookie 'token=b42a5e5845c046c49eec3d01c63365c0.2130706433.1476337615937'" + " '" + httpUrl.trim() + "?" + dDatas + "'</div>");
                    }

                }
            }
        } else {
            crulType = "No Example";
        }
        strings[0] = crulType;
        strings[1] = crulData.toString();
        return strings;
    }


}
