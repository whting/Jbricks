package cn.jbricks.toolkit.exception;

/**
 * User: kelen
 * Date: 2011-9-6 13:42:31
 */
public class ManagerException extends JbricksException {

    private static final long serialVersionUID = -1333549495282643425L;

    public ManagerException(String msg) {
        super(msg);
    }

    public ManagerException(Throwable e) {
        super(e);
    }

    public ManagerException(String msg, Throwable e) {
        super(msg, e);
    }

}