package cn.jbricks.toolkit.exception;

/**
 * User: kelen
 * Date: 2011-9-6 13:42:31
 */
public class ServiceException extends JbricksException {

    private static final long serialVersionUID = -1333549495282643425L;

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Throwable e) {
        super(e);
    }

    public ServiceException(String msg, Throwable e) {
        super(msg, e);
    }

}