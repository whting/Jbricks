package cn.jbricks.toolkit.clazz;

import java.io.File;

/**
 * Created by kuiyuexiang on 15/12/3.
 */

public class JarFinder {
    public JarFinder() {
    }

    public static String getJarFromClasspath(String jarname) {
        String classpath = System.getProperty("java.class.path");
        String[] cpEntries = classpath.split(File.pathSeparator);
        if ((cpEntries == null) || (cpEntries.length == 0)) {
            return null;
        }
        for (String entry : cpEntries) {
            System.out.println(entry);
            if (entry.endsWith(jarname)) {
                return entry;
            }
        }
        return null;
    }
}
