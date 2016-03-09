package cn.jbricks.toolkit.top.gen;

import cn.jbricks.toolkit.clazz.ClassUtil;
import cn.jbricks.toolkit.result.Result;
import cn.jbricks.toolkit.top.annotation.TopConstant;
import cn.jbricks.toolkit.top.annotation.TopEntry;
import cn.jbricks.toolkit.top.annotation.TopFileUpload;
import cn.jbricks.toolkit.top.annotation.TopService;
import cn.jbricks.toolkit.web.rest.constant.ParamName;
import cn.jbricks.toolkit.web.session.constant.SessionAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by kuiyuexiang on 15/12/3.
 */
public abstract class BaseGenerator {
    protected final List<Class<?>> businessClazzes = new ArrayList();
    public static final String TAB = "\t";
    public static final String NEWLINE = "\r\n";
    public static final String LEFT_BRACE = "{";
    public static final String RIGHT_BRACE = "}";
    public static final String SEMICOLON = ";";
    public static final String EQUALS = " = ";

    protected String targetPackage;

    protected abstract void genServices(List<Class<?>> paramList, String paramString)
            throws Exception;

    protected abstract void genConstants(List<Class<?>> paramList, String paramString)
            throws Exception;

    protected abstract void genEntries(List<Class<?>> paramList, String paramString)
            throws Exception;

    public final void execute(List<Class<?>> clazzes, String destPath, String targetPackage) {
        this.targetPackage = targetPackage;
        try {
            genConstants(getClazzesIncludeAnnotation(clazzes, TopConstant.class), destPath);
            genEntries(getClazzesIncludeAnnotation(clazzes, TopEntry.class), destPath);
            genServices(getClazzesIncludeAnnotation(clazzes, TopService.class), destPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected List<Class<?>> getClazzesIncludeAnnotation(List<Class<?>> clazzes, Class annotationClazz) {
        List<Class<?>> consClazzes = new ArrayList();
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return consClazzes;
        }
        for (Class<?> clazz : clazzes) {
            if (clazz.getAnnotation(annotationClazz) != null) {
                consClazzes.add(clazz);
            }
        }
        return consClazzes;
    }

    protected String getConstantsPackage(Class<?> clazz) {
        String module = clazz.getAnnotation(TopConstant.class).value();
        if (StringUtils.isEmpty(module)) {
            return targetPackage + ".constant";
        } else {
            return targetPackage + ".constant." + module;
        }
    }

    protected String getEntryPackage(Class<?> clazz) {
        if (clazz.getAnnotation(TopEntry.class) == null) {
            return clazz.getPackage().getName();
        }
        String module = clazz.getAnnotation(TopEntry.class).module();
        if (StringUtils.isEmpty(module)) {
            return targetPackage + ".model";
        } else {
            return targetPackage + ".model." + module;
        }
    }

    protected String getEntryName(Class<?> clazz) {
        if (clazz.getAnnotation(TopEntry.class) != null && StringUtils.isNotEmpty(clazz.getAnnotation(TopEntry.class).name())) {
            return clazz.getAnnotation(TopEntry.class).name();
        }
        return clazz.getSimpleName();
    }

    protected String getServicePackage(Class<?> clazz) {
        String module = clazz.getAnnotation(TopService.class).value();
        if (StringUtils.isEmpty(module)) {
            return targetPackage + ".service";
        } else {
            return targetPackage + ".service." + module;
        }
    }

    protected File genPackages(File parent, String packageName) {
        if (StringUtils.isBlank(packageName)) {
            return parent;
        }
        if ((parent == null) || (!parent.exists())) {
            throw new RuntimeException("Genpackages,the parent path not exists~");
        }
        String[] paths = packageName.split("\\.");
        File iter = null;
        for (String path : paths) {
            iter = new File(parent, path);
            if (!iter.exists()) {
                iter.mkdirs();
            }
            parent = iter;
        }
        return parent;
    }

    protected void createJavaFile(String name, File packageDir, StringBuilder sb) {
        File newFile = new File(packageDir, name + ".java");
        String content = sb.toString();
        try {
            boolean isExists = newFile.exists();
            if (!isExists) {
                newFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(content.getBytes());
            fos.close();
            if (isExists) {
                System.out.println("[TopGenerator]Existing file " + newFile.getAbsolutePath() + " was overwritten");
            } else {
                System.out.println("[TopGenerator]file " + newFile.getAbsolutePath() + " was generate");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean isPublic(int modifiers) {
        return (modifiers & 0x1) > 0;
    }

    protected boolean isAbstract(int modifiers) {
        return (modifiers & 0x400) > 0;
    }

    protected boolean isFinal(int modifiers) {
        return (modifiers & 0x10) > 0;
    }

    protected boolean isStatic(int modifiers) {
        return (modifiers & 0x8) > 0;
    }

    protected boolean isJsonIgnore(Field field) {
        JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
        if (jsonIgnore != null) {
            return true;
        }
        return false;
    }

    protected void addGetterAndSetterMethod(Field f, StringBuilder fieldsb) {
        String srcFieldName = f.getName();
        String fieldName = srcFieldName.toUpperCase().substring(0, 1) + srcFieldName.substring(1);
        if (f.getType().equals(Boolean.TYPE)) {
            fieldsb.append("\r\n").append("\t").append("public boolean is" + fieldName + "()").append("{").append("\r\n").append("\t").append("\t").append("return this." + srcFieldName).append(";").append("\r\n").append("\t").append("}").append("\r\n").append("\r\n");
        } else {
            fieldsb.append("\r\n").append("\t").append("public " + getTypeName(f) + " get" + fieldName + "()").append("{").append("\r\n").append("\t").append("\t").append("return this." + srcFieldName).append(";").append("\r\n").append("\t").append("}").append("\r\n").append("\r\n");
        }
        fieldsb.append("\t").append("public void set" + fieldName + "(" + getTypeName(f) + " " + srcFieldName + ")").append("{").append("\r\n").append("\t").append("\t").append("this." + srcFieldName + " = " + srcFieldName).append(";").append("\r\n").append("\t").append("}");
    }

    protected void addImports(String srcPackageName, Class<?> parentClazz, Set<String> imports) {
        String parentPackage = parentClazz.getPackage().getName();
        if (parentClazz.getAnnotation(TopConstant.class) != null) {
            parentPackage = getConstantsPackage(parentClazz);
        }
        if (parentClazz.getAnnotation(TopEntry.class) != null) {
            parentPackage = getEntryPackage(parentClazz);
        }
        if (parentClazz.getAnnotation(TopService.class) != null) {
            parentPackage = getServicePackage(parentClazz);
        }
        if (!srcPackageName.equals(parentPackage)) {
            imports.add("import " + parentPackage + "." + parentClazz.getSimpleName() + ";" + "\r\n");
        }
    }

    protected void addImports(String srcPackageName, Method m, Set<String> imports) {
        Class<?> retClass = m.getReturnType();
        Type rType = null;
        if (m.getGenericReturnType() instanceof ParameterizedType) {
            rType = ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0];
            if (rType instanceof ParameterizedType) {
                retClass = (Class) ((ParameterizedType) rType).getRawType();
            } else if (rType instanceof GenericArrayType) {
                retClass = (Class) ((GenericArrayType) rType).getGenericComponentType();
            } else {
                retClass = (Class) rType;
            }
        }
        if (!ClassUtil.isSimpleType(retClass)) {
            if (ClassUtil.isTreeClass(retClass, List.class)) {
                ParameterizedType pt = (ParameterizedType) rType;
                Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
                if ((!ClassUtil.isSimpleType(componentType)) && (!srcPackageName.equals(getEntryPackage(componentType)))) {
                    imports.add("import " + getEntryPackage(componentType) + "." + getEntryName(componentType) + ";" + "\r\n");
                }
                imports.add("import java.util.List;\r\n");
            } else if (ClassUtil.isTreeClass(retClass, Map.class)) {
                ParameterizedType pt = (ParameterizedType) rType;
                Class<?> keyComponentType = (Class) pt.getActualTypeArguments()[0];
                Class<?> valueComponentType = (Class) pt.getActualTypeArguments()[1];
                if ((!ClassUtil.isSimpleType(valueComponentType)) && (!srcPackageName.equals(getEntryPackage(valueComponentType)))) {
                    imports.add("import " + getEntryPackage(valueComponentType) + "." + getEntryName(valueComponentType) + ";" + "\r\n");
                }
                if ((!ClassUtil.isSimpleType(keyComponentType)) && (!srcPackageName.equals(getEntryPackage(keyComponentType)))) {
                    imports.add("import " + getEntryPackage(keyComponentType) + "." + getEntryName(keyComponentType) + ";" + "\r\n");
                }
                imports.add("import java.util.Map;\r\n");
            } else if (ClassUtil.isTreeClass(retClass, Set.class)) {
                ParameterizedType pt = (ParameterizedType) rType;
                Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
                if ((!ClassUtil.isSimpleType(componentType)) && (!srcPackageName.equals(getEntryPackage(componentType)))) {
                    imports.add("import " + getEntryPackage(componentType) + "." + getEntryName(componentType) + ";" + "\r\n");
                }
                imports.add("import java.util.Set;\r\n");
            } else if (!srcPackageName.equals(getEntryPackage(retClass)) && retClass != Result.class) {
                imports.add("import " + getEntryPackage(retClass) + "." + getEntryName(retClass) + ";" + "\r\n");
            }
        }
        Type[] paramTypes = m.getGenericParameterTypes();
        Class<?>[] paramClazzes = m.getParameterTypes();
        if ((paramClazzes == null) || (paramClazzes.length == 0)) {
            return;
        }
        Type type = null;
        Class<?> paramClazz = null;
        for (int index = 0; index < paramClazzes.length; index++) {
            paramClazz = paramClazzes[index];
            type = paramTypes[index];
            if (!ClassUtil.isSimpleType(paramClazz)) {
                if (paramClazz.isArray()) {
                    Class<?> componentType = paramClazz.getComponentType();
                    if ((!ClassUtil.isSimpleType(componentType)) && (!srcPackageName.equals(getEntryPackage(componentType)))) {
                        imports.add("import " + getEntryPackage(componentType) + "." + getEntryName(componentType) + ";" + "\r\n");
                    }
                } else if (ClassUtil.isTreeClass(paramClazz, List.class)) {
                    ParameterizedType pt = (ParameterizedType) type;
                    Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
                    if ((!ClassUtil.isSimpleType(componentType)) && (!srcPackageName.equals(getEntryPackage(componentType)))) {
                        imports.add("import " + getEntryPackage(componentType) + "." + getEntryName(componentType) + ";" + "\r\n");
                    }
                    imports.add("import java.util.List;\r\n");
                } else if (ClassUtil.isTreeClass(paramClazz, Map.class)) {
                    ParameterizedType pt = (ParameterizedType) type;
                    Class<?> keyComponentType = (Class) pt.getActualTypeArguments()[0];
                    Class<?> valueComponentType = (Class) pt.getActualTypeArguments()[1];
                    if ((!ClassUtil.isSimpleType(valueComponentType)) && (!srcPackageName.equals(getEntryPackage(valueComponentType)))) {
                        imports.add("import " + getEntryPackage(valueComponentType) + "." + getEntryName(valueComponentType) + ";" + "\r\n");
                    }
                    if ((!ClassUtil.isSimpleType(keyComponentType)) && (!srcPackageName.equals(getEntryPackage(keyComponentType)))) {
                        imports.add("import " + getEntryPackage(keyComponentType) + "." + getEntryName(keyComponentType) + ";" + "\r\n");
                    }
                    imports.add("import java.util.Map;\r\n");
                } else if (ClassUtil.isTreeClass(paramClazz, Set.class)) {
                    ParameterizedType pt = (ParameterizedType) type;
                    Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
                    if ((!ClassUtil.isSimpleType(componentType)) && (!srcPackageName.equals(getEntryPackage(componentType)))) {
                        imports.add("import " + getEntryPackage(componentType) + "." + getEntryName(componentType) + ";" + "\r\n");
                    }
                    imports.add("import java.util.Set;\r\n");
                } else if (!srcPackageName.equals(getEntryPackage(paramClazz))) {
                    imports.add("import " + getEntryPackage(paramClazz) + "." + getEntryName(paramClazz) + ";" + "\r\n");
                }
            }
        }
    }

    protected void addImports(String srcPackageName, Field f, Set<String> imports) {
        Class<?> fieldType = f.getType();
        if (ClassUtil.isSimpleType(fieldType)) {
            return;
        }
        if (fieldType.isArray()) {
            Class<?> componentType = fieldType.getComponentType();
            System.out.println(componentType);
            if ((!ClassUtil.isSimpleType(componentType)) && (!srcPackageName.equals(getEntryPackage(componentType)))) {
                imports.add("import " + getEntryPackage(componentType) + "." + getEntryName(componentType) + ";" + "\r\n");
            }
        } else if (ClassUtil.isTreeClass(fieldType, List.class)) {
            ParameterizedType pt = (ParameterizedType) f.getGenericType();
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            if ((!ClassUtil.isSimpleType(componentType)) && (!srcPackageName.equals(getEntryPackage(componentType)))) {
                imports.add("import " + getEntryPackage(componentType) + "." + getEntryName(componentType) + ";" + "\r\n");
            }
            imports.add("import java.util.List;\r\n");
        } else if (ClassUtil.isTreeClass(fieldType, Map.class)) {
            ParameterizedType pt = (ParameterizedType) f.getGenericType();
            Class<?> keyComponentType = (Class) pt.getActualTypeArguments()[0];
            Class<?> valueComponentType = (Class) pt.getActualTypeArguments()[1];
            if ((!ClassUtil.isSimpleType(valueComponentType)) && (!srcPackageName.equals(getEntryPackage(valueComponentType)))) {
                imports.add("import " + getEntryPackage(valueComponentType) + "." + getEntryName(valueComponentType) + ";" + "\r\n");
            }
            if ((!ClassUtil.isSimpleType(keyComponentType)) && (!srcPackageName.equals(getEntryPackage(keyComponentType)))) {
                imports.add("import " + getEntryPackage(keyComponentType) + "." + getEntryName(keyComponentType) + ";" + "\r\n");
            }
            imports.add("import java.util.Map;\r\n");
        } else if (ClassUtil.isTreeClass(fieldType, Set.class)) {
            ParameterizedType pt = (ParameterizedType) f.getGenericType();
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            if ((!ClassUtil.isSimpleType(componentType)) && (!srcPackageName.equals(getEntryPackage(componentType)))) {
                imports.add("import " + getEntryPackage(componentType) + "." + getEntryName(componentType) + ";" + "\r\n");
            }
            imports.add("import java.util.Set;\r\n");
        } else if (!srcPackageName.equals(getEntryPackage(fieldType))) {
            imports.add("import " + getEntryPackage(fieldType) + "." + getEntryName(fieldType) + ";" + "\r\n");
        }
    }

    protected String getTypeName(Field f) {
        Class<?> clazz = f.getType();
        if (clazz.isArray()) {
            Class<?> componentType = f.getType().getComponentType();
            return getEntryName(componentType) + "[]";
        }
        if (ClassUtil.isTreeClass(clazz, List.class)) {
            ParameterizedType pt = (ParameterizedType) f.getGenericType();
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            return "List<" + getEntryName(componentType) + ">";
        }
        if (ClassUtil.isTreeClass(clazz, Map.class)) {
            ParameterizedType pt = (ParameterizedType) f.getGenericType();
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            return "Map<" + getEntryName(componentType) + "," + ((Class) pt.getActualTypeArguments()[1]).getSimpleName() + ">";
        }
        if (ClassUtil.isTreeClass(clazz, Set.class)) {
            ParameterizedType pt = (ParameterizedType) f.getGenericType();
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            return "Set<" + getEntryName(componentType) + ">";
        }
        return clazz.getSimpleName();
    }

    protected String getParameterTypeName(Method m, int index) {
        Type paramType = m.getGenericParameterTypes()[index];
        Class<?> paramClazz = m.getParameterTypes()[index];
        if (paramClazz.isArray()) {
            Class<?> componentType = paramClazz.getComponentType();
            return getEntryName(componentType) + "[]";
        }
        if (ClassUtil.isTreeClass(paramClazz, List.class)) {
            ParameterizedType pt = (ParameterizedType) paramType;
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            return "List<" + getEntryName(componentType) + ">";
        }
        if (ClassUtil.isTreeClass(paramClazz, Map.class)) {
            ParameterizedType pt = (ParameterizedType) paramType;
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            return "Map<" + getEntryName(componentType) + "," + ((Class) pt.getActualTypeArguments()[1]).getSimpleName() + ">";
        }
        if (ClassUtil.isTreeClass(paramClazz, Set.class)) {
            ParameterizedType pt = (ParameterizedType) paramType;
            Class<?> componentType = (Class) pt.getActualTypeArguments()[0];
            return "Set<" + getEntryName(componentType) + ">";
        }
        return getEntryName(paramClazz);
    }

    protected void checkUploadDataType(Method m, int index) {
        Class<?> paramClazz = m.getParameterTypes()[index];
        if (!paramClazz.equals(byte[].class)) {
            throw new RuntimeException("the upload field must the 'byte[]' type");
        }
    }

    protected boolean isUploadMethod(Method m) {
        return m.getAnnotation(TopFileUpload.class) != null;
    }

    protected HashMap<String, String[]> getParamMap(Class<?> cls) {
        HashMap<String, String[]> ret = new HashMap();
        try {
            if (cls.getDeclaredMethods().length < 1) {
                return ret;
            }
            Class<?> stubCls = Class.forName(cls.getPackage().getName() + ".impl." + cls.getSimpleName() + "Impl");
            Method[] methodes = stubCls.getDeclaredMethods();
            if ((methodes == null) || (methodes.length < 1)) {
                return ret;
            }
            for (Method m : methodes) {
                ParameterNameDiscoverer p = new LocalVariableTableParameterNameDiscoverer();
                String[] names = p.getParameterNames(m);

                ret.put(m.getName(), names);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    protected boolean isSysParams(String name) {
        if (!ParamName.appId.equals(name) && !ParamName.deviceId.equals(name) && !ParamName.deviceType.equals(name)
                && !ParamName.tid.equals(name) && !SessionAttribute.userId.equals(name)) {
            return false;
        } else {
            return true;
        }
    }

    protected void addCopyRights(StringBuilder contents) {
        contents.append("/**\r\n");
        contents.append(" * CreatedBy TopApi Generator ").append("\r\n");
        contents.append(" */\r\n");
    }

    protected Method[] sortMethods(Method[] methods) {
        if ((methods == null) || (methods.length == 0)) {
            return null;
        }
        List<Method> list = new ArrayList();
        List<String> methodnames = new ArrayList();
        for (Method m : methods) {
            if (m.getAnnotation(RequestMapping.class) != null) {
                list.add(m);
                if (methodnames.contains(m.getName())) {
                    throw new RuntimeException("in a service can't include the same method name." + m.getName());
                }
                methodnames.add(m.getName());
            }
        }
        Collections.sort(list, new Comparator<Method>() {
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return (Method[]) list.toArray(new Method[list.size()]);
    }

    protected Field[] sortFields(Field[] fields) {
        if (true) {
            return fields;
        }
        if ((fields == null) || (fields.length == 0)) {
            return null;
        }
        List<Field> list = new ArrayList();
        for (Field f : fields) {
            list.add(f);
        }
        Collections.sort(list, new Comparator<Field>() {
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return (Field[]) list.toArray(new Field[list.size()]);
    }
}
