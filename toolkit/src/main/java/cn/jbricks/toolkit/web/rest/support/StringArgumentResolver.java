package cn.jbricks.toolkit.web.rest.support;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.URLDecoder;

/**
 * Created by kuiyuexiang on 15/12/25.
 */
public class StringArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String jsonpCallback = "jsonpCallback";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == String.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String value = webRequest.getParameter(parameter.getParameterName());
        if (StringUtils.isNotEmpty(webRequest.getParameter(jsonpCallback))
                && StringUtils.isNotEmpty(value)) {
            value = URLDecoder.decode(value, "utf-8");
        }
        return StringUtils.trim(value);
    }
}
