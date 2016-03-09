package cn.jbricks.toolkit.result;

/**
 * User: kelen
 * Date: 2011-11-9 15:53:20
 */
public class TextResult extends Result<String> {

    public TextResult(int code, String message, String model) {
        super(code, message, model);
    }

    public static TextResult valueOfSuccess() {
        return new TextResult(RS_SUCCESS_DEFAULT.getCode(), null, null);
    }

    public static TextResult valueOfSuccess(String message) {
        return new TextResult(RS_SUCCESS_DEFAULT.getCode(), message, null);
    }

    public static TextResult valueOfError() {
        return new TextResult(RS_FALSE_DEFAULT.getCode(), RS_FALSE_DEFAULT.getMessage(), null);
    }

    public static TextResult valueOfError(String message) {
        return new TextResult(RS_FALSE_DEFAULT.getCode(), message, null);
    }


}