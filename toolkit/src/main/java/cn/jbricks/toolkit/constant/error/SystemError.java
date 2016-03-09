package cn.jbricks.toolkit.constant.error;

import cn.jbricks.toolkit.result.Result;

/**
 * Created by kuiyuexiang on 15/11/17.
 */
public class SystemError {
    public static final int CODE_OFFSET = -1000;

    public static final Result SESSION_INVALID = new Result(ErrorCode.SESSION_INVALID, "登录无效或会话已过期!");
    public static final Result SESSION_TOKEN_DIRTY = new Result(ErrorCode.SESSION_INVALID, "已在其他设备登录，请重新登录!");

    public static final Result APPID_EMPTY = new Result(CODE_OFFSET - 02, "系统错误!", "参数appId不能为空");
    public static final Result VERSION_EMPTY = new Result(CODE_OFFSET - 03, "系统错误!", "参数version不能为空");
    public static final Result TID_EMPTY = new Result(CODE_OFFSET - 05, "系统错误!", "参数tid不能为空");
    public static final Result DEVICEID_EMPTY = new Result(CODE_OFFSET - 04, "系统错误!", "参数deviceId不能为空");
    public static final Result DEVICE_TYPE_EMPTY = new Result(CODE_OFFSET - 05, "系统错误!", "参数deviceType不能为空");

    public static final Result PARAMS_EMPTY = new Result(CODE_OFFSET - 06, "参数{0}不能为空!");

    public static final Result NO_PERMISSION = new Result(ErrorCode.NO_PERMISSION, "您没有相关权限!");


}
