package cn.jbricks.toolkit.web.session.exception;

/**
 * Created by kuiyuexiang on 15/11/19.
 */
public class SessionInvalidException extends Exception {

    private static final long serialVersionUID = -1333549495282643425L;

    public SessionInvalidException(String msg) {
        super(msg);
    }

    public SessionInvalidException(Throwable e) {
        super(e);
    }

    public SessionInvalidException(String msg, Throwable e) {
        super(msg, e);
    }
}
