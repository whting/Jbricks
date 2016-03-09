package cn.jbricks.toolkit.web.session.exception;

/**
 * Created by kuiyuexiang on 15/11/19.
 */
public class NoPermissionException extends Exception {

    private static final long serialVersionUID = -1333549495282643425L;

    public NoPermissionException(String msg) {
        super(msg);
    }

    public NoPermissionException(Throwable e) {
        super(e);
    }

    public NoPermissionException(String msg, Throwable e) {
        super(msg, e);
    }
}
