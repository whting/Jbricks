package cn.jbricks.toolkit.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * User: kelen
 * Date: 2011-11-9 15:53:20
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 3976733653567025460L;
    protected static final Result RS_FALSE_DEFAULT = new Result(-1000, "系统错误");
    protected static final Result RS_SUCCESS_DEFAULT = new Result(1, "默认的成功返回值");

    private int code = -1;
    private String message;
    private T model;

    public Result() {
    }

    public Result(int code, String message, T model) {
        this.code = code;
        this.message = message;
        this.model = model;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result valueOfSuccess() {
        return new Result(RS_SUCCESS_DEFAULT.code, RS_SUCCESS_DEFAULT.getMessage());
    }

    public static Result valueOfSuccess(Object model) {
        return new Result(RS_SUCCESS_DEFAULT.code, RS_SUCCESS_DEFAULT.getMessage(), model);
    }

    public static Result valueOfSuccess(int code, String message, Object model) {
        return new Result(code, message, model);
    }

    public static Result valueOfError() {
        return new Result(RS_FALSE_DEFAULT.code, RS_FALSE_DEFAULT.getMessage());
    }

    public static Result valueOfError(int code, String message) {
        return new Result(code, message, null);
    }

    public static Result valueOfError(String message, Object model) {
        return new Result(RS_FALSE_DEFAULT.code, message, model);
    }

    public static Result valueOfError(String message) {
        return new Result(RS_FALSE_DEFAULT.code, message, null);
    }

    public static Result valueOfError(Object model) {
        return new Result(RS_FALSE_DEFAULT.code, RS_FALSE_DEFAULT.message, model);
    }

    public static Result valueOf(Result result) {
        return new Result(result.getCode(), result.getMessage(), result.getModel());
    }

    /**
     * 请求是否成功。
     *
     * @return 如果成功，则返回<code>true</code>
     * @desc 返回code<=-1，表示执行失败
     * 返回code>=0，表示执行成功
     */
    public boolean isSuccess() {
        return this.code >= RS_SUCCESS_DEFAULT.code;
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result formatMessage(String... arguments) {
        this.message = MessageFormat.format(message, arguments);
        return this;
    }

    /**
     * 取得model对象
     *
     * @return
     */
    public T getModel() {
        return this.model;
    }

    public Result setModel(T model) {
        this.model = model;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Result) {
            if (((Result) o).getCode() == this.getCode()) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new RuntimeException("[Result-equals], o instanceof Result is false");
        }
    }

    @Override
    public String toString() {
        return new StringBuffer().append("[code=").append(this.getCode())
                .append(";message=").append(this.getMessage()).append("]").toString();
    }
}