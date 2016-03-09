package cn.jbricks.toolkit.clazz;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by kuiyuexiang on 15/12/3.
 */
public class ClassUtil {
    private static final Set<Class> simpleClasses = new HashSet();

    private static final Set<Class> collectionClasses = new HashSet();

    static {
        simpleClasses.add(Integer.TYPE);
        simpleClasses.add(Integer.class);
        simpleClasses.add(Long.TYPE);
        simpleClasses.add(Long.class);
        simpleClasses.add(byte.class);
        simpleClasses.add(Byte.class);
        simpleClasses.add(Byte.TYPE);
        simpleClasses.add(Double.TYPE);
        simpleClasses.add(Double.class);
        simpleClasses.add(Boolean.TYPE);
        simpleClasses.add(Boolean.class);
        simpleClasses.add(Character.TYPE);
        simpleClasses.add(Character.class);
        simpleClasses.add(Float.class);
        simpleClasses.add(Float.TYPE);
        simpleClasses.add(Void.TYPE);
        simpleClasses.add(String.class);
        simpleClasses.add(Short.class);
        simpleClasses.add(Short.TYPE);

        collectionClasses.add(List.class);
        collectionClasses.add(Collection.class);
        collectionClasses.add(Map.class);
    }

    public static boolean isSimpleType(Class clazz) {
        return simpleClasses.contains(clazz);
    }

    public static boolean isListType(Class clazz) {
        return clazz == List.class;
    }

    public static boolean isTreeClass(Class<?> childClazz, Class<?> parentClazz) {
        if (childClazz.equals(parentClazz)) {
            return true;
        }
        if (parentClazz.isInterface()) {
            Class<?>[] types = childClazz.getInterfaces();
            for (Class<?> type : types) {
                if (type.equals(parentClazz)) {
                    return true;
                }
            }
        }
        Class sc = childClazz.getSuperclass();
        while (sc != null) {
            if (sc.equals(parentClazz)) {
                return true;
            }
            sc = sc.getSuperclass();
        }
        return false;
    }
//
//    public static Object convert(String value, Class type) {
//        if (type == Void.TYPE) {
//            return null;
//        }
//        if ((type == Boolean.class) || (type == Boolean.TYPE)) {
//            return Boolean.valueOf(value == null ? false : Boolean.parseBoolean(value));
//        }
//        if ((type == Integer.TYPE) || (type == Integer.class)) {
//            return Integer.valueOf(value == null ? 0 : Integer.parseInt(value));
//        }
//        if ((type == Long.TYPE) || (type == Long.class)) {
//            return Long.valueOf(value == null ? 0L : Long.parseLong(value));
//        }
//        if ((type == Float.TYPE) || (type == Float.class)) {
//            return Float.valueOf(value == null ? 0.0F : Float.parseFloat(value));
//        }
//        if (type == String.class) {
//            return value;
//        }
//        if ((type == Byte.class) || (type == Byte.TYPE)) {
//            return Byte.valueOf(Byte.parseByte(value));
//        }
//        if ((type == Double.class) || (type == Double.TYPE)) {
//            return Double.valueOf(value == null ? 0.0D : Double.parseDouble(value));
//        }
//        if ((type == Character.TYPE) || (type == Character.class)) {
//            return Character.valueOf(value.toCharArray()[0]);
//        }
//        if (value == null) {
//            return null;
//        }
//        return JSON.parseObject(value, type);
//    }
//
//    public static Object convertToObj(JSONObject jsonObject, Class<?> clazz, Type type, String key) {
//        if (!isSimpleType(clazz)) {
//            if ((clazz.isArray()) || (isTreeClass(clazz, Set.class)) || (isTreeClass(clazz, List.class))) {
//                JSONArray ja = jsonObject.getJSONArray(key);
//                return ja == null ? null : JSON.parseObject(ja.toJSONString(), clazz);
//            }
//            JSONObject jb = jsonObject.getJSONObject(key);
//            return jb == null ? null : JSON.parseObject(jb.toJSONString(), clazz);
//        }
//        String simpleValue = jsonObject.getString(key);
//        if (clazz.equals(String.class)) {
//            return simpleValue;
//        }
//        if (clazz.equals(Integer.TYPE)) {
//            return Integer.valueOf(simpleValue == null ? 0 : Integer.parseInt(simpleValue));
//        }
//        if (clazz.equals(Integer.class)) {
//            return simpleValue == null ? null : Integer.valueOf(Integer.parseInt(simpleValue));
//        }
//        if (clazz.equals(Long.TYPE)) {
//            return Long.valueOf(simpleValue == null ? 0L : Long.parseLong(simpleValue));
//        }
//        if (clazz.equals(Long.class)) {
//            return simpleValue == null ? null : Long.valueOf(Long.parseLong(simpleValue));
//        }
//        if (clazz.equals(Double.TYPE)) {
//            return Double.valueOf(simpleValue == null ? 0.0D : Double.parseDouble(simpleValue));
//        }
//        if (clazz.equals(Double.class)) {
//            return simpleValue == null ? null : Double.valueOf(Double.parseDouble(simpleValue));
//        }
//        if ((clazz.equals(Boolean.class)) || (clazz.equals(Boolean.TYPE))) {
//            return Boolean.valueOf(simpleValue == null ? false : Boolean.parseBoolean(simpleValue));
//        }
//        if (clazz.equals(Byte.class)) {
//            return simpleValue == null ? null : Byte.valueOf(Byte.parseByte(simpleValue));
//        }
//        if (clazz.equals(Byte.TYPE)) {
//            return Byte.valueOf(simpleValue == null ? 0 : Byte.parseByte(simpleValue));
//        }
//        if (clazz.equals(Character.TYPE)) {
//            return Character.valueOf(simpleValue == null ? '\000' : simpleValue.charAt(0));
//        }
//        if (clazz.equals(Character.class)) {
//            return simpleValue == null ? null : Character.valueOf(simpleValue.charAt(0));
//        }
//        if (clazz.equals(Float.class)) {
//            return simpleValue == null ? null : Float.valueOf(Float.parseFloat(simpleValue));
//        }
//        if (clazz.equals(Float.TYPE)) {
//            return Float.valueOf(simpleValue == null ? 0.0F : Float.parseFloat(simpleValue));
//        }
//        if (clazz.equals(Short.class)) {
//            return simpleValue == null ? null : Short.valueOf(Short.parseShort(simpleValue));
//        }
//        if (clazz.equals(Short.TYPE)) {
//            return Short.valueOf(simpleValue == null ? 0 : Short.parseShort(simpleValue));
//        }
//        throw new RuntimeException("the param found error data type");
//    }

    /**
     * 获得包下面的所有的class
     *
     * @param pack package完整名称
     * @return List包含所有class的实例
     */
    public static List<Class<?>> getClasssFromPackage(String pack) {
        List<Class<?>> clazzs = new ArrayList<Class<?>>();

        // 是否循环搜索子包
        boolean recursive = true;

        // 包名字
        String packageName = pack;
        // 包名对应的路径名称
        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();

                System.out.println("[ClassUtil]get class from package,url=" + url);
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);
                } else if ("jar".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    getClasssFromJarFile(filePath, packageName, clazzs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazzs;
    }

    /**
     * 在package对应的路径下找到所有的class
     *
     * @param packageName package名称
     * @param filePath    package对应的路径
     * @param recursive   是否查找子package
     * @param clazzs      找到class以后存放的集合
     */
    private static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class<?>> clazzs) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 在给定的目录下找到所有的文件，并且进行条件过滤
        File[] dirFiles = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
                boolean acceptClass = file.getName().endsWith("class");// 接受class文件
                return acceptDir || acceptClass;
            }
        });

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从jar文件中读取指定目录下面的所有的class文件
     *
     * @param jarPaht  jar文件存放的位置
     * @param filePaht 指定的文件目录
     * @return 所有的的class的对象
     */
    private static void getClasssFromJarFile(String jarPaht, String filePaht, List<Class<?>> clazzs) {

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPaht);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        List<JarEntry> jarEntryList = new ArrayList<JarEntry>();

        Enumeration<JarEntry> ee = jarFile.entries();
        while (ee.hasMoreElements()) {
            JarEntry entry = (JarEntry) ee.nextElement();
            // 过滤我们出满足我们需求的东西
            if (entry.getName().startsWith(filePaht) && entry.getName().endsWith(".class")) {
                jarEntryList.add(entry);
            }
        }
        for (JarEntry entry : jarEntryList) {
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - 6);

            try {
                clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
