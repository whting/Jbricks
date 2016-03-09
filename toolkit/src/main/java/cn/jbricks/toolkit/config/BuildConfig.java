package cn.jbricks.toolkit.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuiyuexiang on 15/12/25.
 */
public class BuildConfig {

    private static Map<String, String> configMap = new HashMap<String, String>();

    private static String debug;
    private static String domain;

    public static boolean isDebug() {
        return Boolean.valueOf(debug);
    }

    public void setDebug(String debug) {
        BuildConfig.debug = debug;
    }

    public static String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        BuildConfig.domain = domain;
    }

    public static Map<String, String> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, String> configMap) {
        BuildConfig.configMap.putAll(configMap);
    }

    public static String getConfig(String name) {
        return configMap.get(name);
    }
}
