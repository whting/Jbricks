package cn.jbricks.toolkit.web.session;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by kuiyuexiang on 16/1/28.
 */
public class SessionCache implements Serializable {
    private String token;
    private String userName;
    private String realName;
    private Integer loginType;
    private Date gmtTokened;
    private Integer options;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Date getGmtTokened() {
        return gmtTokened;
    }

    public void setGmtTokened(Date gmtTokened) {
        this.gmtTokened = gmtTokened;
    }

    public Integer getOptions() {
        return options;
    }

    public void setOptions(Integer options) {
        this.options = options;
    }
}
