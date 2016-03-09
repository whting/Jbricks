package cn.jbricks.toolkit.clazz;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by kuiyuexiang on 15/12/3.
 */
public class ClassLoaderTool {
    public ClassLoaderTool() {
    }

    public static void loadJar(String jarpath) {
        List<String> jarUrls = new ArrayList();
        File f = new File(jarpath);
        if (f.isFile()) {
            jarUrls.add(f.getAbsolutePath());
        } else {
            ReadJarFile df = new ReadJarFile(jarpath, new String[]{"jar", "zip"});
            jarUrls = df.getFiles();
        }
        loadAllClasses(jarUrls, getClassLoader(jarUrls.toArray(new String[jarUrls.size()])));
    }

    private static void loadAllClasses(List<String> jarUrls, URLClassLoader classloader) {
        if ((jarUrls == null) || (jarUrls.size() == 0)) {
            return;
        }
        for (String jarUrl : jarUrls) {
            if (jarUrl.startsWith("file:")) {
                jarUrl = jarUrl.substring("file:".length());
            }
            loadClass(jarUrl, classloader);
        }
    }

    private static URLClassLoader getClassLoader(Object[] jarURLs) {
        URL[] urls = new URL[jarURLs.length];
        for (int i = 0; i < jarURLs.length; i++) {
            try {
                urls[i] = new URL(jarURLs[i].toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return new URLClassLoader(urls);
    }

    private static void loadClass(String jarFileName, URLClassLoader classLoader) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<JarEntry> en = jarFile.entries();
        while (en.hasMoreElements()) {
            JarEntry je = (JarEntry) en.nextElement();
            String name = je.getName();
            String s5 = name.replace('/', '.');
            if (s5.lastIndexOf(".class") > 0) {
                String className = je.getName().substring(0, je.getName().length() - ".class".length()).replace('/', '.');
                try {
                    classLoader.loadClass(className);
                    System.out.println(className);
                } catch (ClassNotFoundException e) {
                    System.out.println("NO CLASS: " + className);
                } catch (NoClassDefFoundError e) {
                    System.out.println("NO CLASS: " + className);
                }
            }
        }
    }
}
