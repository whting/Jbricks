package cn.jbricks.toolkit.valueobject;

import java.io.Serializable;

/**
 * User: kuiyuexiang
 * Date: 2012-10-29
 * Time: 上午8:05
 */
public class BaseVO implements Serializable {


    private static final long serialVersionUID = 1L;

    protected Long uid;                       //唯一的数字Id

    public Long getUid() {
        return uid;
    }
}
