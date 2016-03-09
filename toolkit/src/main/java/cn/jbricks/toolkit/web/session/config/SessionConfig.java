package cn.jbricks.toolkit.web.session.config;

/**
 * Created by kuiyuexiang on 16/1/17.
 */
public class SessionConfig {

    public static String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
