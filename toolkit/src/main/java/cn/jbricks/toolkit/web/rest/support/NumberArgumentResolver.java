package cn.jbricks.toolkit.web.rest.support;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by kuiyuexiang on 15/12/25.
 */
public class NumberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class type = parameter.getParameterType();
        return type == Long.class || type == Integer.class || type == Double.class || type == Float.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class type = parameter.getParameterType();
        String value = webRequest.getParameter(parameter.getParameterName());
        if (StringUtils.isEmpty(value) || "0".equals(value)) {
            return null;
        }
        if (type == Long.class) {
            return Long.valueOf(value);
        }
        if (type == Integer.class) {
            return Integer.valueOf(value);
        }
        if (type == Double.class) {
            return Double.valueOf(value);
        }
        if (type == Float.class) {
            return Float.valueOf(value);
        }
        return null;
    }
}
