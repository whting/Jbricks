package cn.jbricks.toolkit.exception;

/**
 * User: kuiyuexiang
 * Date: 2012-12-29
 * Time: 下午9:51
 */
public class JbricksException extends RuntimeException {

    private static final long serialVersionUID = -1333549495282643425L;

    public JbricksException(String msg) {
        super(msg);
    }

    public JbricksException(Throwable e) {
        super(e);
    }

    public JbricksException(String msg, Throwable e) {
        super(msg, e);
    }
}
