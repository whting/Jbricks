package cn.jbricks.toolkit.web.session.exception;

/**
 * Created by kuiyuexiang on 15/11/19.
 */
public class SessionTokenDirtyException extends Exception {

    private static final long serialVersionUID = -1333549495282643425L;

    public SessionTokenDirtyException(String msg) {
        super(msg);
    }

    public SessionTokenDirtyException(Throwable e) {
        super(e);
    }

    public SessionTokenDirtyException(String msg, Throwable e) {
        super(msg, e);
    }
}
