package cn.jbricks.toolkit.top.gen;

import cn.jbricks.toolkit.clazz.ClassUtil;
import cn.jbricks.toolkit.result.Result;
import cn.jbricks.toolkit.top.annotation.TopEntry;
import cn.jbricks.toolkit.top.annotation.TopOnlyServer;
import cn.jbricks.toolkit.web.rest.constant.ParamName;
import cn.jbricks.toolkit.web.session.constant.SessionAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by kuiyuexiang on 15/12/3.
 */
public class AndroidRPCGenerator
        extends BaseGenerator {

    protected void genServices(List<Class<?>> clazzes, String destPath)
            throws Exception {
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return;
        }
        for (Class<?> clazz : clazzes) {
            if (!clazz.isAnnotationPresent(TopOnlyServer.class)) {
                genServiceSource(clazz, destPath);
            }
        }
    }


    protected void genEntries(List<Class<?>> clazzes, String destPath)
            throws Exception {
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return;
        }
        for (Class<?> clazz : clazzes) {
            if (!clazz.isAnnotationPresent(TopOnlyServer.class)) {
                genEntrySource(clazz, destPath);
            }
        }
    }

    protected void genConstants(List<Class<?>> clazzes, String destPath)
            throws Exception {
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return;
        }
        for (Class<?> clazz : clazzes) {
            if (!clazz.isAnnotationPresent(TopOnlyServer.class)) {
                genConstantSource(clazz, destPath);
            }
        }
    }

    private void genConstantSource(Class<?> clazz, String destPath)
            throws Exception {
        if (clazz.isInterface()) {
            throw new RuntimeException("The TopConstant class must not be interface");
        }
        File dir = new File(destPath);
        String packageName = getConstantsPackage(clazz);
        String simpleClassname = clazz.getSimpleName();
        File packageDir = genPackages(dir, packageName);

        Field[] fields = sortFields(clazz.getFields());
        StringBuilder sb = new StringBuilder();


        sb.append("package " + packageName).append(";").append("\r\n").append("\r\n");
        addCopyRights(sb);


        sb.append("public class " + simpleClassname).append("{").append("\r\n");
        if ((fields != null) && (fields.length > 0)) {
            for (Field f : fields) {
                if ((!isPublic(f.getModifiers())) || (!isStatic(f.getModifiers()))) {
                    continue;
                }
                if (!isFinal(f.getModifiers())) {
                    if (!f.getType().equals(String.class)) {
                        sb.append("\t").append("public static ").append(f.getType().getSimpleName()).append(" ").append(f.getName()).append(" ")
                                .append(" = ").append(" ").append(f.get(clazz.newInstance()) != null ? f.get(clazz.newInstance()) : "").append(";").append("\r\n");
                    } else {
                        sb.append("\t").append("public static ").append(f.getType().getSimpleName()).append(" ").append(f.getName())
                                .append(" = ").append("\"").append(f.get(clazz.newInstance()) != null ? f.get(clazz.newInstance()) : "").append("\"").append(";").append("\r\n");
                    }
                } else if (!f.getType().equals(String.class)) {
                    sb.append("\t").append("public static final ").append(f.getType().getSimpleName()).append(" ").append(f.getName())
                            .append(" = ").append(f.get(clazz.newInstance()) != null ? f.get(clazz.newInstance()) : "").append(";").append("\r\n");
                } else {
                    sb.append("\t").append("public static final ").append(f.getType().getSimpleName()).append(" ").append(f.getName())
                            .append(" = ").append("\"").append(f.get(clazz.newInstance()) != null ? f.get(clazz.newInstance()) : "").append("\"").append(";").append("\r\n");
                }
            }
        }
        sb.append("}");

        createJavaFile(simpleClassname, packageDir, sb);
    }

    private void genEntrySource(Class<?> clazz, String destPath)
            throws Exception {
        if (clazz.getAnnotation(TopEntry.class).ignore()) {
            return;
        }
        File dir = new File(destPath);
        String packageName = getEntryPackage(clazz);
        String simpleClassname = getEntryName(clazz);
        File packageDir = genPackages(dir, packageName);

        StringBuilder allcontent = new StringBuilder();
        allcontent.append(genEntrySource(clazz));
        createJavaFile(simpleClassname, packageDir, allcontent);
    }

    private StringBuilder genEntrySource(Class<?> clazz)
            throws Exception {
        Class<?> parentClazz = clazz.getSuperclass();

        String packageName = getEntryPackage(clazz);
        String simpleClassname = getEntryName(clazz);

        StringBuilder header = new StringBuilder();
        Set<String> imports = new HashSet();
        StringBuilder fieldsb = new StringBuilder();
        StringBuilder getsetsb = new StringBuilder();
        StringBuilder allcontent = new StringBuilder();


        allcontent.append("package " + packageName).append(";").append("\r\n").append("\r\n");
        addCopyRights(allcontent);


        boolean extendTopEntry = (parentClazz != null) && (parentClazz.getAnnotation(TopEntry.class) != null);
        boolean isFlat = clazz.getAnnotation(TopEntry.class) != null && clazz.getAnnotation(TopEntry.class).flat();
        if (isFlat && parentClazz.getSuperclass() != null) {
            if (parentClazz.getSuperclass() != Object.class) {
                addImports(packageName, parentClazz.getSuperclass(), imports);
                header.append("public class " + simpleClassname).append(" extends ").append(parentClazz.getSuperclass().getSimpleName()).append("{").append("\r\n");
            }
        } else if (extendTopEntry) {
            addImports(packageName, parentClazz, imports);
            header.append("public class " + simpleClassname).append(" extends ").append(parentClazz.getSimpleName()).append("{").append("\r\n");
        } else {
            imports.add("import java.io.Serializable;\r\n");
            header.append("public class " + simpleClassname + " implements Serializable").append("{").append("\r\n");
        }
        header.append("\r\n").append("\t").append("private static final long serialVersionUID = 1L;").append("\r\n");

        if (isFlat && parentClazz.getSuperclass() != null) {
            Field[] fields = sortFields(parentClazz.getDeclaredFields());
            if ((fields != null) && (fields.length > 0)) {
                fieldsb.append("\r\n");
                for (Field f : fields) {
                    if (!isStatic(f.getModifiers()) && !isFinal(f.getModifiers()) && !isJsonIgnore(f)) {
                        addImports(packageName, f, imports);
                        fieldsb.append("\t").append("private " + getTypeName(f) + " " + f.getName()).append(";").append("\r\n");
                        addGetterAndSetterMethod(f, getsetsb);
                    }
                }
            }
        }

        Field[] fields = sortFields(clazz.getDeclaredFields());
        if ((fields != null) && (fields.length > 0)) {
            fieldsb.append("\r\n");
            for (Field f : fields) {
                if (!isStatic(f.getModifiers()) && !isFinal(f.getModifiers()) && !isJsonIgnore(f)) {
                    addImports(packageName, f, imports);
                    fieldsb.append("\t").append("private " + getTypeName(f) + " " + f.getName()).append(";").append("\r\n");
                    addGetterAndSetterMethod(f, getsetsb);
                }
            }
        }
        for (String ele : imports) {
            allcontent.append(ele);
        }
        allcontent.append("\r\n").append(header).append(fieldsb).append(getsetsb).append("\r\n").append("}");
        return allcontent;
    }

    private void genServiceSource(Class<?> clazz, String destPath) {
        if (!clazz.isInterface()) {
            throw new RuntimeException("The TopService draft must interface");
        }
        File dir = new File(destPath);
        String packageName = getServicePackage(clazz);
        String simpleClassname = clazz.getSimpleName();
        File packageDir = genPackages(dir, packageName);

        StringBuilder header = new StringBuilder();
        Set<String> imports = new HashSet();
        StringBuilder methodsb = new StringBuilder();
        StringBuilder constructSb = new StringBuilder();

        StringBuilder allcontent = new StringBuilder();
        allcontent.append("package " + packageName).append(";").append("\r\n").append("\r\n");
        addCopyRights(allcontent);
        for (Class<?> ele : this.businessClazzes) {
            imports.add("import " + ele.getName() + ";" + "\r\n");
        }
        header.append("public class " + simpleClassname).append("{").append("\r\n");
        imports.add("import cn.bihu.app.net.BihuResultCallback;\r\n");
        imports.add("import cn.bihu.common.okhttp.request.OkHttpRequest;\r\n");
        imports.add("import " + this.targetPackage + ".constant.TopConfig;\r\n");
        imports.add("import " + this.targetPackage + ".constant.TopSession;\r\n");
        imports.add("import cn.bihu.app.top.util.TopMap;\r\n");

        HashMap<String, String[]> paramNames = getParamMap(clazz);
        String requestUrl = getRequesturl(clazz);
        Method[] methods = sortMethods(clazz.getDeclaredMethods());
        if ((methods != null) && (methods.length > 0)) {
            for (Method m : methods) {
                RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
                TopOnlyServer topOnlyServer = m.getAnnotation(TopOnlyServer.class);
                if (requestMapping == null || requestMapping.value() == null
                        || requestMapping.value().length == 0 || topOnlyServer != null) {
                    continue;
                }
                addImports(packageName, m, imports);

                methodsb.append("\r\n").append("\r\n").append("\t").append("public static void " + m.getName());
                methodsb.append("(");
                String[] params = paramNames.get(m.getName());
                if ((params != null) && (params.length > 0)) {
                    for (int index = 0; index < params.length; index++) {
                        if (!isSysParams(params[index])) {
                            methodsb.append(getParameterTypeName(m, index) + " " + params[index]);
                            methodsb.append(",");
                        }
                    }
                }
                String callbackRetTypeName = getAndroidCallbackRetTypeName(m);
                methodsb.append("BihuResultCallback<" + callbackRetTypeName + "> callback")
                        .append(")").append("{").append("\r\n");

                //requestUrl
                methodsb.append("\t\t").append("String requestUrl = TopConfig.domain + \"" + requestUrl + requestMapping.value()[0] + "?")
                        .append(ParamName.appId + "=\"+" + "TopConfig.appId")
                        .append("\r\n\t\t\t\t")
                        .append("+\"&" + ParamName.version + "=\"+" + "TopConfig.version")
                        .append("+\"&" + SessionAttribute.userId + "=\"+" + "TopSession.userId")
                        .append("+\"&" + ParamName.tid + "=\"+" + "TopConfig.tid")
                        .append(";").append("\r\n\r\n");

                //params
                methodsb.append("\t\t").append("TopMap params = new TopMap();").append("\r\n");
                if ((params != null) && (params.length > 0)) {
                    for (int index = 0; index < params.length; index++) {
                        if (!isSysParams(params[index])) {
                            methodsb.append("\t\t").append("params.put(\"" + params[index] + "\"," + params[index] + ");").append("\r\n");
                        }
                    }
                    methodsb.append("\r\n");
                    methodsb.append("\t\t").append("params.put(\"" + SessionAttribute.sessionId + "\",TopSession.sessionId);").append("\r\n");
                    methodsb.append("\t\t").append("params.put(\"" + SessionAttribute.token + "\",TopSession.token);").append("\r\n");
                    methodsb.append("\t\t").append("params.put(\"" + SessionAttribute.signature + "\",TopSession.signature);").append("\r\n");
                    methodsb.append("\t\t").append("params.put(\"" + ParamName.deviceId + "\",TopConfig.deviceId);").append("\r\n");
                    methodsb.append("\t\t").append("params.put(\"" + ParamName.deviceType + "\",TopConfig.deviceType);").append("\r\n");
                }

                methodsb.append("\t\t").append("new OkHttpRequest.Builder().url(requestUrl).params(params).post(callback);");
                methodsb.append("\r\n").append("\t").append("}");
            }
        }
        for (String ele : imports) {
            allcontent.append(ele);
        }
        allcontent.append("\r\n").append(header).append(constructSb).append(methodsb).append("\r\n").append("}");
        createJavaFile(simpleClassname, packageDir, allcontent);
    }

    protected String getAndroidCallbackRetTypeName(Method m) {
        Class<?> cla = m.getReturnType();
        if (cla != Result.class) {
            throw new RuntimeException("ReturnType must be " + Result.class.getName());
        }

        Class<?> clazz = null;
        if (m.getGenericReturnType() instanceof ParameterizedType) {
            Type type = ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0];
            if (type instanceof ParameterizedType) {
                clazz = (Class) ((ParameterizedType) type).getRawType();
                if (ClassUtil.isTreeClass(clazz, List.class)) {
                    Class<?> componentType = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
                    return "List<" + getEntryName(componentType) + ">";
                } else if (ClassUtil.isTreeClass(clazz, Map.class)) {
                    Class<?> keyType = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
                    Class<?> componentType = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
                    return "Map<" + getEntryName(keyType) + "," + getEntryName(componentType) + ">";
                } else if (ClassUtil.isTreeClass(clazz, Set.class)) {
                    Class<?> componentType = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
                    return "Set<" + getEntryName(componentType) + ">";
                }
            } else {
                if (type instanceof GenericArrayType) {
                    Class<?> componentType = (Class) ((GenericArrayType) type).getGenericComponentType();
                    return getEntryName(componentType) + "[]";
                }
                clazz = (Class) type;
            }
        } else {
            clazz = Boolean.class;
        }
        if ((clazz.equals(Void.class)) || (clazz.equals(Void.TYPE))) {
            return "Void";
        }
        if (clazz.equals(String.class)) {
            return "String";
        }
        if ((clazz.equals(Integer.TYPE)) || (clazz.equals(Integer.class))) {
            return "Integer";
        }
        if ((clazz.equals(Long.TYPE)) || (clazz.equals(Long.class))) {
            return "Long";
        }
        if ((clazz.equals(Double.TYPE)) || (clazz.equals(Double.class))) {
            return "Double";
        }
        if ((clazz.equals(Float.TYPE)) || (clazz.equals(Float.class))) {
            return "Float";
        }
        if ((clazz.equals(Boolean.TYPE)) || (clazz.equals(Boolean.class))) {
            return "Boolean";
        }
        if ((clazz.equals(Byte.TYPE)) || (clazz.equals(Byte.class))) {
            return "Byte";
        }
        if ((clazz.equals(Character.TYPE)) || (clazz.equals(Character.class))) {
            return "Character";
        }
        if ((clazz.equals(Short.class)) || (clazz.equals(Short.TYPE))) {
            return "Short";
        }
        return getEntryName(clazz);
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

