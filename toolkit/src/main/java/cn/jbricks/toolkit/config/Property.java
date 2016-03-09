package cn.jbricks.toolkit.config;

/**
 * Created by kuiyuexiang on 16/1/9.
 */
public class Property {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}
