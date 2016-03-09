package cn.jbricks.toolkit.web.session;

import cn.jbricks.toolkit.top.annotation.TopEntry;
import cn.jbricks.toolkit.util.UniqID;
import cn.jbricks.toolkit.web.session.config.SessionConfig;
import cn.jbricks.toolkit.web.session.constant.SessionAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * jbricks session的实现
 *
 * @author kelen
 */
@TopEntry(name = "Session", module = "user")
public class Session {

    private String sessionId;
    private String token;
    private String userId;
    private String appId;
    private String signature;

    private String userName;
    private String realName;
    private Integer loginType;
    @JsonIgnore
    private Date gmtTokened;
    @JsonIgnore
    private Integer options;

    @JsonIgnore
    private HttpServletRequest request;
    @JsonIgnore
    private HttpServletResponse response;

    @JsonIgnore
    private Map<String, Cookie> cookiesMap = new HashedMap();

    public Session(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        this.initCookiesMap();
        this.initSessionFromCookie();
        this.initSessionFromHeader();
        this.initSessionFromParameter();

        if (StringUtils.isEmpty(this.sessionId)) {
            this.sessionId = DigestUtils.md5Hex(UniqID.getInstance().getUniqID());
        }
    }

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotEmpty(sessionId) && StringUtils.isNotEmpty(token)
                && StringUtils.isNotEmpty(userId) && StringUtils.equals(this.signature, generateSignature());
    }

    public boolean signatureValid() {
        return StringUtils.equals(this.signature, generateSignature());
    }

    public String getSessionId() {
        return sessionId;
    }

    public void clearSession() {
        this.token = null;
        this.userId = null;
        flushSignature();
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSignature() {
        return signature;
    }

    public void flushSignature() {
        this.signature = generateSignature();
    }

    private void initCookiesMap() {
        Cookie[] cookies = this.request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                this.cookiesMap.put(cookie.getName(), cookie);
            }
        }
    }

    private String getCookieValue(String name) {
        Cookie cookie = this.cookiesMap.get(name);
        if (cookie != null && StringUtils.equals(cookie.getDomain(), request.getServerName())) {
            return cookie != null ? cookie.getValue() : "";
        }
        return "";
    }

    private void initSessionFromCookie() {
        sessionId = getCookieValue(SessionAttribute.sessionId);
        token = getCookieValue(SessionAttribute.token);
        userId = getCookieValue(SessionAttribute.userId);
        appId = getCookieValue(SessionAttribute.appId);
        signature = getCookieValue(SessionAttribute.signature);
    }

    private void initSessionFromHeader() {
        if (StringUtils.isEmpty(sessionId)) {
            sessionId = request.getHeader(SessionAttribute.sessionId);
        }
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader(SessionAttribute.token);
        }
        if (StringUtils.isEmpty(userId)) {
            userId = request.getHeader(SessionAttribute.userId);
        }
        if (StringUtils.isEmpty(appId)) {
            appId = request.getHeader(SessionAttribute.appId);
        }
        if (StringUtils.isEmpty(signature)) {
            signature = request.getHeader(SessionAttribute.signature);
        }
    }

    private void initSessionFromParameter() {
        if (StringUtils.isEmpty(sessionId)) {
            sessionId = request.getParameter(SessionAttribute.sessionId);
        }
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(SessionAttribute.token);
        }
        if (StringUtils.isEmpty(userId)) {
            userId = request.getParameter(SessionAttribute.userId);
        }
        if (StringUtils.isEmpty(appId)) {
            appId = request.getParameter(SessionAttribute.appId);
        }
        if (StringUtils.isEmpty(signature)) {
            signature = request.getParameter(SessionAttribute.signature);
        }
    }

    public void reloadSession(HttpServletRequest request) {
        sessionId = request.getParameter(SessionAttribute.sessionId);
        token = request.getParameter(SessionAttribute.token);
        userId = request.getParameter(SessionAttribute.userId);
        appId = request.getParameter(SessionAttribute.appId);
        signature = request.getParameter(SessionAttribute.signature);
    }

    /**
     * session安全签名
     * 签名算法：
     * 1.sessionId,token,userId每个属性取前4个字母
     * 2.MD5编码
     *
     * @return
     */
    private String generateSignature() {
        StringBuilder signature = new StringBuilder();
        signature.append(SessionConfig.secretKey);
        signature.append(getChars(this.sessionId));
        signature.append(getChars(this.token));
        signature.append(getChars(this.userId));
        signature.append(getChars(this.appId));

        return DigestUtils.md5Hex(signature.toString());
    }

    private String getChars(String value) {
        if (value == null) {
            return "";
        }
        if (value.length() >= 3) {
            return value.substring(0, 2);
        } else {
            return value;
        }
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}

