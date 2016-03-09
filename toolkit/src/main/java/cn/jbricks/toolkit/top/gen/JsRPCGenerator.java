package cn.jbricks.toolkit.top.gen;

/**
 * Created by kuiyuexiang on 15/12/20.
 */

import cn.jbricks.toolkit.clazz.ClassUtil;
import cn.jbricks.toolkit.top.annotation.TopConstant;
import cn.jbricks.toolkit.top.annotation.TopEntry;
import cn.jbricks.toolkit.top.annotation.TopOnlyServer;
import cn.jbricks.toolkit.top.util.JavaNameUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class JsRPCGenerator
        extends BaseGenerator {
    private static String CONSNAME = "CST";
    private static String RPCNAME = "RPC";
    private static String MODELNAME = "M";
    private static String SUBDIR = "model";

    private Map<String, String> contentMap = new HashMap<String, String>();

    public JsRPCGenerator(String consPrefix, String rpcPrefix, String modelPrefix, String subDir) {
        CONSNAME = consPrefix;
        RPCNAME = rpcPrefix;
        MODELNAME = modelPrefix;
        SUBDIR = subDir;
    }

    private void genConstantSource(Class<?> clazz, String destPath)
            throws Exception {
        StringBuilder contents = new StringBuilder();
        TopConstant topConstant = clazz.getAnnotation(TopConstant.class);
        String module = "";
        if (StringUtils.isNotEmpty(topConstant.value())) {
            module = topConstant.value().replaceAll("\\.", "_");
            module = JavaNameUtil.underlineToCamel(module);
            module = JavaNameUtil.initialToUpperCase(module);
        }

        if (contentMap.get(JavaNameUtil.initialToUpperCase(CONSNAME) + module + ".js") == null) {
            addCopyRights(contents);

            contents.append("\r\n").append("\r\n");
            contents.append("if (!window.Constant) {\r\n");
            contents.append("\twindow.Constant = {}\r\n");
            contents.append("}\r\n");
        }

        Field[] fields = sortFields(clazz.getDeclaredFields());
        if ((fields != null) && (fields.length > 0)) {
            for (Field f : fields) {
                if ((!isPublic(f.getModifiers())) || (!isStatic(f.getModifiers()))) {
                    continue;
                }
                if (!f.getType().equals(String.class)) {
                    contents.append(JavaNameUtil.initialToUpperCase(CONSNAME)).append(".").append(clazz.getSimpleName())
                            .append("_").append(f.getName()).append(" = ")
                            .append(f.get(clazz.newInstance()) != null ? f.get(clazz.newInstance()) : "").append(";\r\n");
                } else {
                    contents.append(JavaNameUtil.initialToUpperCase(CONSNAME)).append(".").append(clazz.getSimpleName())
                            .append("_").append(f.getName()).append(" = \"")
                            .append(f.get(clazz.newInstance()) != null ? f.get(clazz.newInstance()) : "").append("\";\r\n");
                }
            }
        }
        contents.append("\r\n");
        createJavaFile(JavaNameUtil.initialToUpperCase(CONSNAME) + module + ".js", new File(destPath + "/" + SUBDIR, CONSNAME), contents);
    }

    //
//    var UserService = {
//          searchUser: function (realName, insuranceId, gmtCreatedStart, gmtCreatedEnd, pagination, onSuccess, onError) {
//          var url = "http://localhost:8080/jbricks/admin/user/search_user";
//          var params={
    //             realName: realName,
//                insuranceId: insuranceId,
//                gmtCreatedStart: gmtCreatedStart,
//                gmtCreatedEnd: gmtCreatedEnd,
//                pagination: pagination
//        };
//        util.ajax(url, params, onSuccess, onError);
//    }
//}
    private void genServiceSource(Class<?> clazz, String destPath) {
        if (!clazz.isInterface()) {
            throw new RuntimeException("The TopService draft must interface");
        }
        File dir = new File(destPath);
        String packageName = getServicePackage(clazz);
        String simpleClassname = clazz.getSimpleName();
        File packageDir = genPackages(dir, packageName);

        StringBuilder methodsb = new StringBuilder();

        StringBuilder allcontent = new StringBuilder();
        addCopyRights(allcontent);

        allcontent.append("var " + simpleClassname + " = {");

        HashMap<String, String[]> paramNames = getParamMap(clazz);
        Method[] methods = sortMethods(clazz.getDeclaredMethods());
        if ((methods != null) && (methods.length > 0)) {
            for (Method m : methods) {
                RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
                TopOnlyServer topOnlyServer = m.getAnnotation(TopOnlyServer.class);
                if (requestMapping == null || requestMapping.value() == null
                        || requestMapping.value().length == 0 || topOnlyServer != null) {
                    continue;
                }
                methodsb.append("\r\n").append("\r\n").append("\t").append(m.getName() + ":function(");
                String[] params = paramNames.get(m.getName());
                if ((params != null) && (params.length > 0)) {
                    for (int index = 0; index < params.length; index++) {
                        if (!isSysParams(params[index])) {
                            methodsb.append(params[index]);
                            methodsb.append(",");
                        }
                    }
                }
                methodsb.append("onSuccess, onError").append(")").append("{").append("\r\n");

                //requestUrl
                methodsb.append("\t\t").append("var url = \"" + getRequesturl(clazz) + requestMapping.value()[0] + "\";").append("\r\n\r\n");

                //params
                methodsb.append("\t\t").append("var params ={").append("\r\n");
                if ((params != null) && (params.length > 0)) {
                    boolean isFirst = true;
                    for (int index = 0; index < params.length; index++) {
                        if (!isSysParams(params[index])) {
                            if (!isFirst) {
                                methodsb.append(",\r\n");
                            }
                            methodsb.append("\t\t\t").append(params[index] + ":" + params[index]);
                            isFirst = false;
                        }
                    }
                }

                methodsb.append("\r\n\t\t};\r\n");
                if (isUploadMethod(m)) {
                    methodsb.append("\t\tutil.ajaxUpload(url, params, onSuccess, onError);\r\n");
                } else {
                    methodsb.append("\t\tutil.ajax(url, params, onSuccess, onError);\r\n");
                }
                methodsb.append("\t").append("},");
            }
        }
        if (methodsb.toString().endsWith(",")) {
            methodsb.deleteCharAt(methodsb.length() - 1);
        }
        allcontent.append("\r\n").append(methodsb).append("\r\n").append("}");
        createJavaFile(simpleClassname + ".js", packageDir, allcontent);
    }


    private void genEntrySource(Class<?> clazz, String destPath) {
        String clazzName = getEntryName(clazz);
        StringBuilder contents = new StringBuilder();
        addCopyRights(contents);

        Set<String> imports = new HashSet();

        StringBuilder propertiesSb = new StringBuilder();
        propertiesSb.append("@interface " + getEntryFullName(clazz, clazzName) + " : " + getExtendName(clazz)).append("\r\n");

        boolean isFlat = clazz.getAnnotation(TopEntry.class) != null && clazz.getAnnotation(TopEntry.class).flat();
        Class<?> parentClazz = clazz.getSuperclass();
        if (isFlat && parentClazz.getSuperclass() != null) {
            Field[] fields = sortFields(parentClazz.getDeclaredFields());
            if ((fields != null) && (fields.length > 0)) {
                for (Field f : fields) {
                    if (isJsonIgnore(f)) {
                        continue;
                    }
                    addImports(imports, f.getType(), f.getGenericType());
                    if (!ClassUtil.isSimpleType(f.getType()) || f.getType() == String.class) {
                        propertiesSb.append("@property (nonatomic,strong) ").append(javaTypeToObjectCType(f.getType(), f.getGenericType())).append(" " + f.getName()).append(";").append("\r\n");
                    } else {
                        propertiesSb.append("@property (nonatomic,assign) ").append(javaTypeToObjectCType(f.getType(), f.getGenericType())).append(" " + f.getName()).append(";").append("\r\n");
                    }
                }
            }
        }
        propertiesSb.append("\r\n");
        Field[] fields = sortFields(clazz.getDeclaredFields());
        if ((fields != null) && (fields.length > 0)) {
            for (Field f : fields) {
                if (isJsonIgnore(f)) {
                    continue;
                }
                addImports(imports, f.getType(), f.getGenericType());
                if (!ClassUtil.isSimpleType(f.getType()) || f.getType() == String.class) {
                    propertiesSb.append("@property (nonatomic,strong) ").append(javaTypeToObjectCType(f.getType(), f.getGenericType())).append(" " + f.getName()).append(";").append("\r\n");
                } else {
                    propertiesSb.append("@property (nonatomic,assign) ").append(javaTypeToObjectCType(f.getType(), f.getGenericType())).append(" " + f.getName()).append(";").append("\r\n");
                }
            }
        }
        propertiesSb.append("\r\n").append("@end");
        for (String ele : imports) {
            contents.append(ele);
        }
        contents.append("\r\n").append("@protocol " + getEntryFullName(clazz, clazzName) + " <NSObject>\r\n")
                .append("@end").append("\r\n");

        contents.append("\r\n").append(propertiesSb);
        createJavaFile(getEntryFullName(clazz, clazzName) + ".h", new File(destPath + "/" + SUBDIR, MODELNAME), contents);
    }

    private String getEntryFullName(Class<?> clazz, String clazzName) {
        TopEntry topConstant = clazz.getAnnotation(TopEntry.class);
        if (topConstant != null && StringUtils.isNotEmpty(topConstant.module())) {
            String packageName = topConstant.module().replaceAll("\\.", "_");
            packageName = JavaNameUtil.underlineToCamel(packageName);
            packageName = JavaNameUtil.initialToUpperCase(packageName);
            clazzName = packageName + clazzName;
        }
        return "BH" + MODELNAME + clazzName;
    }


    private String getExtendName(Class<?> clazz) {
        Class<?> superClazz = clazz.getSuperclass();

        boolean isFlat = clazz.getAnnotation(TopEntry.class) != null && clazz.getAnnotation(TopEntry.class).flat();
        if (isFlat) {
            superClazz = superClazz.getSuperclass();
        }
        if (superClazz.isAnnotationPresent(TopEntry.class)) {
            return "BH" + MODELNAME + superClazz.getSimpleName();
        }
        return "BHBaseModel";
//        return MODELNAME + "Object";
    }

    protected void createJavaFile(String name, File parentDir, StringBuilder sb) {
        File newFile = new File(parentDir, name);
        String content = sb.toString();
        String temp = contentMap.get(name);
        if (temp != null) {
            content = temp + content;
        }
        contentMap.put(name, content);
        try {
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String javaTypeToObjectCType(Class<?> cls, Type genericType) {
        if (cls == null) {
            return null;
        }
        if ((cls == Void.TYPE) || (cls == Void.class)) {
            return "id";
        }
        if ((cls == Integer.TYPE) || (cls == Integer.class)) {
            return "int";
        }
        if ((cls == Boolean.TYPE) || (cls == Boolean.class)) {
            return "BOOL";
        }
        if ((cls == Character.TYPE) || (cls == Byte.TYPE)) {
            return "char";
        }
        if ((cls == Float.TYPE) || (cls == Float.class)) {
            return "float";
        }
        if ((cls == Double.TYPE) || (cls == Double.class)) {
            return "double";
        }
        if ((cls == Long.TYPE) || (cls == Long.class)) {
            return "long";
        }
        if (cls == Date.class) {
            return "NSString*";
        }
        if (cls == Object.class) {
            return "NSObject*";
        }
        if (ClassUtil.isTreeClass(cls, Number.class)) {
            return "NSNumber*";
        }
        if (ClassUtil.isTreeClass(cls, String.class)) {
            return "NSString*";
        }
        if (ClassUtil.isTreeClass(cls, JSONObject.class)) {
            return "NSDictionary*";
        }
        if (ClassUtil.isTreeClass(cls, JSONArray.class)) {
            return "NSArray*";
        }
        if (cls.isArray()) {
            if (cls.equals(Byte.class)) {
                return "NSData*";
            }
            return "NSArray*";
        }
        if (ClassUtil.isTreeClass(cls, List.class)) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            if (componentType.isArray()) {
                return "NSArray<NSArray>*";
            }
            if ((!componentType.equals(Object.class)) && (componentType.getAnnotation(TopEntry.class) != null)) {
                return "NSArray<" + getEntryFullName(componentType, componentType.getSimpleName()) + ">*";
            }
            return "NSArray*";
        }
        if (ClassUtil.isTreeClass(cls, Map.class)) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> keyComponentType = (Class) pt.getActualTypeArguments()[0];
            Class<?> valueComponentType = (Class) pt.getActualTypeArguments()[1];
            Class<?>[] typeArray = new Class[2];
            typeArray[0] = keyComponentType;
            typeArray[1] = valueComponentType;

            StringBuilder ret = new StringBuilder("NSDictionary");
            boolean hasAdd = false;
            for (int i = 0; i < 2; i++) {
                if ((!typeArray[i].equals(Object.class)) && (typeArray[i].getAnnotation(TopEntry.class) != null)) {
                    if (!hasAdd) {
                        hasAdd = true;
                        ret.append("<");
                    } else {
                        ret.append(", ");
                    }
                    ret.append(getEntryFullName(typeArray[i], typeArray[i].getSimpleName()));
                }
            }
            if (hasAdd) {
                ret.append(">");
            }
            ret.append("*");
            return ret.toString();
        }
        if (ClassUtil.isTreeClass(cls, Set.class)) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            if ((!componentType.equals(Object.class)) && (componentType.getAnnotation(TopEntry.class) != null)) {
                return "NSArray<" + getEntryFullName(componentType, componentType.getSimpleName()) + ">*";
            }
            return "NSSet*";
        }
        return getEntryFullName(cls, cls.getSimpleName()) + "*";
    }

    private void addImports(Set<String> imports, Class<?> clazz, Type genericType) {
        if (ClassUtil.isSimpleType(clazz)) {
            return;
        }
        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            if ((!componentType.equals(Object.class)) && (componentType.getAnnotation(TopEntry.class) != null)) {
                imports.add("#import \"" + getEntryFullName(componentType, componentType.getSimpleName()) + ".h\"" + "\r\n");
            }
            return;
        }
        if (ClassUtil.isTreeClass(clazz, List.class)) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            if ((componentType != Object.class) && (componentType.getAnnotation(TopEntry.class) != null)) {
                imports.add("#import \"" + getEntryFullName(componentType, componentType.getSimpleName()) + ".h\"" + "\r\n");
            }
            return;
        }
        if (ClassUtil.isTreeClass(clazz, Map.class)) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> keyComponentType = (Class) pt.getActualTypeArguments()[0];
            Class<?> valueComponentType = (Class) pt.getActualTypeArguments()[1];
            Class<?>[] typeArray = new Class[2];
            typeArray[0] = keyComponentType;
            typeArray[1] = valueComponentType;
            for (int i = 0; i < 2; i++) {
                if ((typeArray[i] != Object.class) && (typeArray[i].getAnnotation(TopEntry.class) != null)) {
                    imports.add("#import \"" + getEntryFullName(typeArray[i], typeArray[i].getSimpleName()) + ".h\"" + "\r\n");
                }
            }
            return;
        }
        if (ClassUtil.isTreeClass(clazz, Set.class)) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            if ((componentType != Object.class) && (componentType.getAnnotation(TopEntry.class) != null)) {
                imports.add("#import \"" + getEntryFullName(componentType, componentType.getSimpleName()) + ".h\"" + "\r\n");
            }
            return;
        }
        if (clazz != Date.class) {
            imports.add("#import \"" + getEntryFullName(clazz, clazz.getSimpleName()) + ".h\"" + "\r\n");
        }
    }

    private void genFolder(String destPath, String folderName) {
        File dir = new File(destPath, folderName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    protected void genConstants(List<Class<?>> clazzes, String destPath)
            throws Exception {
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return;
        }
        contentMap.clear();
        for (Class<?> clazz : clazzes) {
            if (!clazz.isAnnotationPresent(TopOnlyServer.class)) {
                genFolder(destPath, SUBDIR + "/" + CONSNAME);
                genConstantSource(clazz, destPath);
                System.out.println(clazz.getName());
            }
        }
    }

    protected void genEntries(List<Class<?>> clazzes, String destPath)
            throws Exception {
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return;
        }
        contentMap.clear();
        for (Class<?> clazz : clazzes) {
            if (clazz.getAnnotation(TopEntry.class).ignore()) {
                continue;
            }
            if (!clazz.isAnnotationPresent(TopOnlyServer.class)) {
//                genFolder(destPath, SUBDIR + "/" + MODELNAME);
//                genEntrySource(clazz, destPath);
//                System.out.println(clazz.getName());
            }
        }
    }

    protected void genServices(List<Class<?>> clazzes, String destPath)
            throws Exception {
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return;
        }
        contentMap.clear();
        for (Class<?> clazz : clazzes) {
            if (!clazz.isAnnotationPresent(TopOnlyServer.class)) {
                genFolder(destPath, SUBDIR + "/" + RPCNAME);
                genServiceSource(clazz, destPath);
                System.out.println(clazz.getName());
            }
        }
    }

    private String getRequesturl(Class<?> clazz) {
        try {
            Class<?> stubCls = Class.forName(clazz.getPackage().getName() + ".impl." + clazz.getSimpleName() + "Impl");
            RequestMapping requestMapping = stubCls.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                return requestMapping.value()[0];
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }
}
