package cn.jbricks.toolkit.top.gen;

/**
 * Created by kuiyuexiang on 15/12/20.
 */

import cn.jbricks.toolkit.clazz.ClassUtil;
import cn.jbricks.toolkit.result.Result;
import cn.jbricks.toolkit.top.annotation.TopConstant;
import cn.jbricks.toolkit.top.annotation.TopEntry;
import cn.jbricks.toolkit.top.annotation.TopOnlyServer;
import cn.jbricks.toolkit.top.annotation.TopService;
import cn.jbricks.toolkit.top.util.JavaNameUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class OCRPCGenerator
        extends BaseGenerator {
    private static final String DEFINE = "#define";
    private static final String END_IF = "#endif";
    private static final String IF_NOT_DEFINE = "#ifndef";
    private static final String IMPORTS = "#import";
    private static String CONSNAME = "CST";
    private static String RPCNAME = "RPC";
    private static String MODELNAME = "M";
    private static String SUBDIR = "model";

    public OCRPCGenerator() {
    }

    public OCRPCGenerator(String consPrefix, String rpcPrefix, String modelPrefix, String subDir) {
        CONSNAME = consPrefix;
        RPCNAME = rpcPrefix;
        MODELNAME = modelPrefix;
        SUBDIR = subDir;
    }

    private void genContstantSource(Class<?> clazz, String destPath)
            throws Exception {
        genFolder(destPath, SUBDIR + "/" + CONSNAME);
        String clazzName = clazz.getSimpleName();
        StringBuilder contents = new StringBuilder();
        addCopyRights(contents);

        TopConstant topConstant = clazz.getAnnotation(TopConstant.class);
        String packageName = "";
        if (StringUtils.isNotEmpty(topConstant.value())) {
            packageName = topConstant.value().replaceAll("\\.", "_");
            packageName = JavaNameUtil.underlineToCamel(packageName);
            packageName = JavaNameUtil.initialToUpperCase(packageName);
        }

        contents.append("\r\n").append("\r\n");
        contents.append("#ifndef " + getContstantName(clazz, "")).append("\r\n");
        contents.append("#define").append(" " + getContstantName(clazz, "")).append("\r\n").append("\r\n");

        Field[] fields = sortFields(clazz.getDeclaredFields());
        if ((fields != null) && (fields.length > 0)) {
            for (Field f : fields) {
                if ((!isPublic(f.getModifiers())) || (!isStatic(f.getModifiers()))) {
                    continue;
                }
                if (!f.getType().equals(String.class)) {
                    contents.append("#define").append("\t").append("k" + packageName + clazzName + "_" + f.getName())
                            .append(" ").append(" ").append(f.get(clazz.newInstance())).append("\r\n");
                } else {
                    contents.append("#define").append("\t").append("k" + packageName + clazzName + "_" + f.getName())
                            .append(" ").append(" @\"").append(f.get(clazz.newInstance())).append("\"").append("\r\n");
                }
            }
        }
        contents.append("\r\n");
        contents.append("#endif");
        createJavaFile(getContstantName(clazz, CONSNAME) + ".h", new File(destPath + "/" + SUBDIR, CONSNAME), contents);
    }


    private String getContstantName(Class<?> clazz, String consname) {
        TopConstant topConstant = clazz.getAnnotation(TopConstant.class);
        String packageName = "";
        if (StringUtils.isNotEmpty(topConstant.value())) {
            packageName = topConstant.value().replaceAll("\\.", "_");
            packageName = JavaNameUtil.underlineToCamel(packageName);
            packageName = JavaNameUtil.initialToUpperCase(packageName);
        }
        return "BH" + consname + packageName + clazz.getSimpleName();
    }


    private void genServiceImplSource(Class<?> clazz, String destPath) {
        String clazzName = clazz.getSimpleName();
        Map<String, String[]> paramNames = getParamMap(clazz);
        Method[] methods = sortMethods(clazz.getDeclaredMethods());

        TopService topService = clazz.getAnnotation(TopService.class);
        String packageName = "";
        if (StringUtils.isNotEmpty(topService.value())) {
            packageName = topService.value().replaceAll("\\.", "_");
            packageName = JavaNameUtil.underlineToCamel(packageName);
            packageName = JavaNameUtil.initialToUpperCase(packageName);
        }

        Set<String> imports = new HashSet();
        StringBuilder contents = new StringBuilder();
        StringBuilder methodsb = new StringBuilder();
        addCopyRights(contents);

        imports.add("#import \"BHConstantTopConfig.h\"" + "\r\n");
        imports.add("#import \"" + getServiceName(clazz, clazzName) + ".h\"" + "\r\n");
        for (Class<?> ele : this.businessClazzes) {
            imports.add("#import \"" + CONSNAME + ele.getSimpleName() + ".h\"" + "\r\n");
        }

        methodsb.append(addRequest(clazz));

        methodsb.append("@implementation " + getServiceName(clazz, clazzName)).append("\r\n").append("\r\n");

        methodsb.append(addSharedInstance(clazz));

        String baseUrl = getRequesturl(clazz);
        if ((methods != null) && (methods.length > 0)) {
            for (Method m : methods) {
                RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
                if (requestMapping == null || requestMapping.value() == null || requestMapping.value().length == 0) {
                    continue;
                }
                if (m.isAnnotationPresent(TopOnlyServer.class)) {
                    continue;
                }
                if (isPublic(m.getModifiers())) {
                    addMethodImpls(clazz, m, paramNames, methodsb, baseUrl, (TopService) clazz.getAnnotation(TopService.class));
                }
            }
        }
        methodsb.append("@end").append("\r\n");
        for (String ele : imports) {
            contents.append(ele);
        }
        contents.append("\r\n").append(methodsb).append("\r\n");
        createJavaFile(getServiceName(clazz, clazzName) + ".m", new File(destPath + "/" + SUBDIR, RPCNAME), contents);
    }


    private StringBuilder addRequest(Class<?> clazz) {
        String clazzName = clazz.getSimpleName();
        Method[] methods = sortMethods(clazz.getDeclaredMethods());

        StringBuilder methodsb = new StringBuilder();
        methodsb.append("@interface " + getServiceName(clazz, clazzName) + "()").append("\r\n").append("\r\n");
        for (Method m : methods) {
            RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
            if (requestMapping == null || requestMapping.value() == null || requestMapping.value().length == 0) {
                continue;
            }
            if (isPublic(m.getModifiers())) {
                methodsb.append("@property (nonatomic, strong) BHRequest *" + m.getName() + "Request;\r\n");
            }
        }
        methodsb.append("@end").append("\r\n").append("\r\n");
        return methodsb;
    }

    private StringBuilder addSharedInstance(Class<?> clazz) {
        StringBuilder methodsb = new StringBuilder();
        methodsb.append("+ (instancetype)sharedInstance {\n");
        methodsb.append("\tstatic " + getServiceName(clazz, clazz.getSimpleName()) + " *sharedInstance = nil;\n");
        methodsb.append("\tstatic dispatch_once_t onceToken;\n");
        methodsb.append("\tif (sharedInstance == nil) {\n");
        methodsb.append("\t\tdispatch_once(&onceToken, ^{\n");
        methodsb.append("\t\t\tsharedInstance = [[" + getServiceName(clazz, clazz.getSimpleName()) + " alloc] init];\n");
        methodsb.append("\t\t});\n");
        methodsb.append("\t}\n");
        methodsb.append("\treturn sharedInstance;\r\n");
        methodsb.append("}\r\n");
        methodsb.append("\r\n");
        return methodsb;
    }

    private void addMethodImpls(Class<?> clazz, Method m, Map<String, String[]> paramNames, StringBuilder methodSb, String baseUrl, TopService topService) {
        String[] names = (String[]) paramNames.get(m.getName());
        Class<?>[] paramClazzes = m.getParameterTypes();

        addMethodDefines(methodSb, paramNames, m);

        methodSb.append("{").append("\r\n").append("\r\n");

        String requestUrl = getRequesturl(clazz);
        RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);

        methodSb.append("\t").append("NSString *baseUrl = [NSString stringWithFormat:@\"%@" + requestUrl + requestMapping.value()[0]
                + "\",kTopConfig_domain];")
                .append("\r\n");


        methodSb.append("\t").append("NSDictionary *queryParams = @{").append("\r\n");
        if ((names != null) && (names.length > 0)) {
            boolean isFirst = true;
            for (int index = 0; index < names.length; index++) {
                if (isSysParams(names[index])) {
                    continue;
                }
                if (!isFirst) {
                    methodSb.append(",").append("\r\n");
                }
                methodSb.append("\t").append("\t").append("@\"" + names[index] + "\":" + convertParmaArg(names[index], paramClazzes[index]));
                isFirst = false;
            }
        }
        methodSb.append("\r\n\t};\r\n\r\n");
        String callbackRetTypeName = getIosCallbackRetTypeName(m);
        methodSb.append("\tNSString *modelClassName =@\"" + callbackRetTypeName + "\";\r\n\r\n");

        methodSb.append("\t_" + m.getName() + "Request = [[BHRequest alloc] initWithBaseUrl:baseUrl queryParams:queryParams modelClassName: modelClassName];\r\n");
        methodSb.append("\t_" + m.getName() + "Request.serviceContext = serviceContext;\r\n");
        methodSb.append("\t[_" + m.getName() + "Request sendRequest];\r\n");

        methodSb.append("}").append("\r\n").append("\r\n");
    }

    private String convertParmaArg(String name, Class<?> type) {
        if ((type == Integer.TYPE) || (type == Integer.class)) {
            return "[NSNumber numberWithLong:" + name + "Arg]";
        }
        if ((type == Float.TYPE) || (type == Float.class)) {
            return "[NSNumber numberWithLong:" + name + "Arg]";
        }
        if ((type == Double.TYPE) || (type == Double.class)) {
            return "[NSNumber numberWithLong:" + name + "Arg]";
        }
        if ((type == Long.TYPE) || (type == Long.class)) {
            return "[NSNumber numberWithLong:" + name + "Arg]";
        }
        return name + "Arg?" + name + "Arg:@\"\"";
    }

    private void genServiceHeaderSource(Class<?> clazz, String destPath) {
        String clazzName = clazz.getSimpleName();
        Map<String, String[]> paramNames = getParamMap(clazz);
        Method[] methods = sortMethods(clazz.getDeclaredMethods());

        StringBuilder contents = new StringBuilder();
        addCopyRights(contents);

        Set<String> imports = new HashSet();
        imports.add("#import <Foundation/Foundation.h>\n");
        imports.add("#import \"BHBaseService.h\"\r\n");
        imports.add("#import \"BHServiceContext.h\"\r\n");
        imports.add("#import \"BHModel.h\"\r\n");

        StringBuilder methodSb = new StringBuilder();

        methodSb.append("@interface " + getServiceName(clazz, clazzName) + " : BHBaseService").append("\r\n").append("\r\n");
        methodSb.append("+ (instancetype)sharedInstance;").append("\r\n\r\n");
        if ((methods != null) && (methods.length > 0)) {
            for (Method m : methods) {
                RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
                if (requestMapping == null || requestMapping.value() == null || requestMapping.value().length == 0) {
                    continue;
                }
                if (m.isAnnotationPresent(TopOnlyServer.class)) {
                    continue;
                }
                if (isPublic(m.getModifiers())) {
                    addMethodDefines(methodSb, paramNames, m);
                    methodSb.append(";").append("\r\n\r\n");
                }
            }
        }
        methodSb.append("@end").append("\r\n");
        for (String ele : imports) {
            contents.append(ele);
        }
        contents.append("\r\n").append("\r\n").append(methodSb);
        createJavaFile(getServiceName(clazz, clazzName) + ".h", new File(destPath + "/" + SUBDIR, RPCNAME), contents);
    }

    private String getServiceName(Class<?> clazz, String clazzName) {
        TopService topService = clazz.getAnnotation(TopService.class);
        if (StringUtils.isNotEmpty(topService.value())) {
            String packageName = topService.value().replaceAll("\\.", "_");
            packageName = JavaNameUtil.underlineToCamel(packageName);
            packageName = JavaNameUtil.initialToUpperCase(packageName);
            clazzName = packageName + clazzName;
        }
        return "BH" + RPCNAME + clazzName;
    }

    private void addMethodDefines(StringBuilder methodSb, Map<String, String[]> paramNames, Method m) {
        methodSb.append("- (void)").append(m.getName());
        String[] names = (String[]) paramNames.get(m.getName());
        Type[] paramTypes = m.getGenericParameterTypes();
        Class<?>[] paramClazzes = m.getParameterTypes();
        if ((names != null) && (names.length > 0)) {
            boolean isFirst = true;
            for (int index = 0; index < names.length; index++) {
                if (isSysParams(names[index])) {
                    continue;
                }
                if (isFirst) {
                    methodSb.append("Width" + JavaNameUtil.initialToUpperCase(names[index]) + ":(").append(javaTypeToObjectCType(paramClazzes[index], paramTypes[index])).append(")" + names[index] + "Arg");
                } else {
                    methodSb.append(" ").append(names[index]).append(":(").append(javaTypeToObjectCType(paramClazzes[index], paramTypes[index])).append(")" + names[index] + "Arg");
                }
                isFirst = false;
            }
            if (isFirst) {
                methodSb.append("WidthServiceContext:(BHServiceContext *)serviceContext");
            } else {
                methodSb.append(" serviceContext:(BHServiceContext *)serviceContext");
            }
        } else {
            methodSb.append("WidthServiceContext:(BHServiceContext *)serviceContext");
        }
    }

    protected void addImports(Set<String> imports, Method m) {
        addImports(imports, m.getReturnType(), m.getGenericReturnType());
        Class<?>[] paramClazzes = m.getParameterTypes();
        Type[] paramGenericTypes = m.getGenericParameterTypes();
        if ((paramClazzes == null) || (paramClazzes.length == 0)) {
            return;
        }
        for (int index = 0; index < paramClazzes.length; index++) {
            addImports(imports, paramClazzes[index], paramGenericTypes[index]);
        }
    }

    private void genEntryImplSource(Class<?> clazz, String destPath) {
        String clazzName = getEntryName(clazz);
        StringBuilder contents = new StringBuilder();
        addCopyRights(contents);

        contents.append("#import").append(" \"").append(getEntryFullName(clazz, clazzName) + ".h\"").append("\r\n");
        contents.append("\r\n");
        contents.append("@implementation " + getEntryFullName(clazz, clazzName)).append("\r\n").append("\r\n");

        contents.append("@end");
        createJavaFile(getEntryFullName(clazz, clazzName) + ".m", new File(destPath + "/" + SUBDIR, MODELNAME), contents);
    }


    private void genEntryHeaderSource(Class<?> clazz, String destPath) {
        String clazzName = getEntryName(clazz);
        StringBuilder contents = new StringBuilder();
        addCopyRights(contents);

        Set<String> imports = new HashSet();
        addSuperClassImports(clazz, imports);

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

    private void addSuperClassImports(Class<?> clazz, Set<String> imports) {
        Class<?> superClazz = clazz.getSuperclass();
        boolean isFlat = clazz.getAnnotation(TopEntry.class) != null && clazz.getAnnotation(TopEntry.class).flat();
        if (isFlat) {
            superClazz = superClazz.getSuperclass();
        }

        if (superClazz.isAnnotationPresent(TopEntry.class)) {
            imports.add("#import \"" + getEntryFullName(superClazz, superClazz.getSimpleName()) + ".h\"" + "\r\n");
        } else {
            imports.add("#import \"BHBaseModel.h\"\r\n");
        }
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

    protected String getObjectCObjectType(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        if (cls == Void.TYPE) {
            return null;
        }
        if (cls.isArray()) {
            if (cls.equals(Byte.class)) {
                return "NSData";
            }
            return "NSArray";
        }
        if (cls.getName().indexOf('.') < 0) {
            return "NSNumber";
        }
        if (cls == Object.class) {
            return "NSObject";
        }
        if (ClassUtil.isTreeClass(cls, Number.class)) {
            return "NSNumber";
        }
        if (cls == String.class) {
            return "NSString";
        }
        if (ClassUtil.isTreeClass(cls, JSONObject.class)) {
            return "NSDictionary";
        }
        if (ClassUtil.isTreeClass(cls, JSONArray.class)) {
            return "NSArray";
        }
        if (ClassUtil.isTreeClass(cls, List.class)) {
            return "NSArray";
        }
        if (ClassUtil.isTreeClass(cls, Map.class)) {
            return "NSDictionary";
        }
        if (ClassUtil.isTreeClass(cls, Set.class)) {
            return "NSSet";
        }
        return cls.getSimpleName();
    }

    private void genFolder(String destPath, String folderName) {
        File dir = new File(destPath, folderName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    protected void genServices(List<Class<?>> clazzes, String destPath)
            throws Exception {
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return;
        }
        for (Class<?> clazz : clazzes) {
            if (!clazz.isAnnotationPresent(TopOnlyServer.class)) {
                genFolder(destPath, SUBDIR + "/" + RPCNAME);

                genServiceHeaderSource(clazz, destPath);
                genServiceImplSource(clazz, destPath);
            }
        }
    }


    protected void genConstants(List<Class<?>> clazzes, String destPath)
            throws Exception {
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return;
        }

        StringBuilder contents = new StringBuilder();
        for (Class<?> clazz : clazzes) {
            if (!clazz.isAnnotationPresent(TopOnlyServer.class)) {
                genContstantSource(clazz, destPath);
                contents.append("#import \"" + getContstantName(clazz, CONSNAME) + ".h\"\r\n");
            }
        }
        createJavaFile("BHConstant.h", new File(destPath + "/" + SUBDIR, CONSNAME), contents);
    }

    protected void genEntries(List<Class<?>> clazzes, String destPath)
            throws Exception {
        if ((clazzes == null) || (clazzes.size() == 0)) {
            return;
        }

        StringBuilder contents = new StringBuilder();
        for (Class<?> clazz : clazzes) {
            if (clazz.getAnnotation(TopEntry.class).ignore()) {
                continue;
            }
            if (!clazz.isAnnotationPresent(TopOnlyServer.class)) {
                genFolder(destPath, SUBDIR + "/" + MODELNAME);

                genEntryHeaderSource(clazz, destPath);
                genEntryImplSource(clazz, destPath);

                contents.append("#import \"" + getEntryFullName(clazz, clazz.getSimpleName()) + ".h\"\r\n");
            }
        }
        createJavaFile("BHModel.h", new File(destPath + "/" + SUBDIR, MODELNAME), contents);
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

    protected String getIosCallbackRetTypeName(Method m) {
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
                    return getEntryFullName(componentType, componentType.getSimpleName());
                } else if (ClassUtil.isTreeClass(clazz, Map.class)) {
                    Class<?> keyType = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
                    Class<?> componentType = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
                    return getEntryFullName(componentType, componentType.getSimpleName());
                } else if (ClassUtil.isTreeClass(clazz, Set.class)) {
                    Class<?> componentType = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
                    return getEntryFullName(componentType, componentType.getSimpleName());
                }
            } else {
                if (type instanceof GenericArrayType) {
                    Class<?> componentType = (Class) ((GenericArrayType) type).getGenericComponentType();
                    return getEntryFullName(componentType, componentType.getSimpleName());
                }
                clazz = (Class) type;
            }
        } else {
            clazz = Boolean.class;
        }
        if ((clazz == Void.TYPE) || (clazz == Void.class)) {
            return "id";
        }
        if ((clazz == Integer.TYPE) || (clazz == Integer.class)) {
            return "int";
        }
        if ((clazz == Boolean.TYPE) || (clazz == Boolean.class)) {
            return "BOOL";
        }
        if ((clazz == Character.TYPE) || (clazz == Byte.TYPE)) {
            return "char";
        }
        if ((clazz == Float.TYPE) || (clazz == Float.class)) {
            return "float";
        }
        if ((clazz == Double.TYPE) || (clazz == Double.class)) {
            return "double";
        }
        if ((clazz == Long.TYPE) || (clazz == Long.class)) {
            return "long";
        }
        if (clazz == Date.class) {
            return "NSString*";
        }
        if (clazz == Object.class) {
            return "NSObject*";
        }

        return getEntryFullName(clazz, clazz.getSimpleName());
    }
}
