package cn.jbricks.toolkit.web.session;

import org.springframework.util.Assert;

/**
 * Created by kuiyuexiang on 15/11/19.
 */
public class SessionHolder {

    private static final ThreadLocal<Session> sessionHolder = new ThreadLocal<Session>();

    public static void clearSession() {
        sessionHolder.remove();
    }

    public static void setSession(Session session) {
        Assert.notNull(session, "Only non-null Session instances are permitted");
        sessionHolder.set(session);
    }

    public static Session getSession() {
        return sessionHolder.get();
    }
}
