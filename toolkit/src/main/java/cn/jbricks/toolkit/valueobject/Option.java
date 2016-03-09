package cn.jbricks.toolkit.valueobject;

/**
 * User: kuiyuexiang
 * Date: 2012-11-07
 * Time: 下午11:40
 */
public class Option {

    private Integer value;
    private String name;

    public Option(Integer value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Option) {
            if (this.value == ((Option) obj).getValue()) {
                return true;
            }
        }
        return false;
    }
}
