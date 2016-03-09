package cn.jbricks.toolkit.web.rest.interceptors;

import cn.jbricks.toolkit.constant.error.SystemError;
import cn.jbricks.toolkit.web.rest.constant.ParamName;
import cn.jbricks.toolkit.web.rest.exception.IllegalParamsException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kuiyuexiang on 15/11/17.
 */
public class RestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StringUtils.isEmpty(request.getParameter(ParamName.appId))) {
            throw new IllegalParamsException(SystemError.APPID_EMPTY);
        }
        if (StringUtils.isEmpty(request.getParameter(ParamName.version))) {
            throw new IllegalParamsException(SystemError.VERSION_EMPTY);
        }
        if (StringUtils.isEmpty(request.getParameter(ParamName.deviceId))) {
            throw new IllegalParamsException(SystemError.DEVICEID_EMPTY);
        }
        if (StringUtils.isEmpty(request.getParameter(ParamName.deviceType))) {
            throw new IllegalParamsException(SystemError.DEVICE_TYPE_EMPTY);
        }
        if (StringUtils.isEmpty(request.getParameter(ParamName.tid))) {
            throw new IllegalParamsException(SystemError.TID_EMPTY);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        return;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        return;
    }
}
