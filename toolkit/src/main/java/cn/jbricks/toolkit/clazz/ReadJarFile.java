package cn.jbricks.toolkit.clazz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuiyuexiang on 15/12/3.
 */
public class ReadJarFile {
    List<String> jarList = new ArrayList();
    List<String> filesURL = new ArrayList();

    public ReadJarFile(String jarFileName, String[] strings) {
        File f = new File(jarFileName);
        File[] fl = f.listFiles();
        for (File file : fl) {
            for (String str : strings) {
                if (file.getName().endsWith(str)) {
                    this.jarList.add(file.getName());
                    this.filesURL.add(file.toURI().toString());
                }
            }
        }
    }

    public List<String> getFiles() {
        return this.filesURL;
    }
}
