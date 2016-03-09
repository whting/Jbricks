package cn.jbricks.toolkit.web.rest.exception;

import cn.jbricks.toolkit.result.Result;

/**
 * Created by kuiyuexiang on 15/11/19.
 */
public class IllegalParamsException extends Exception {

    private Result result;

    public IllegalParamsException(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
}
