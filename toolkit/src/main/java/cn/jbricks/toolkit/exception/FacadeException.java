package cn.jbricks.toolkit.exception;

/**
 * User: kuiyuexiang
 * Date: 2012-12-29
 * Time: 下午9:51
 */
public class FacadeException extends JbricksException {

    private static final long serialVersionUID = -1333549495282643425L;

    public FacadeException(String msg) {
        super(msg);
    }

    public FacadeException(Throwable e) {
        super(e);
    }

    public FacadeException(String msg, Throwable e) {
        super(msg, e);
    }
}
