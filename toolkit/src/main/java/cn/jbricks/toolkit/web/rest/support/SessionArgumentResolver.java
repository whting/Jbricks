package cn.jbricks.toolkit.web.rest.support;

import cn.jbricks.toolkit.web.session.Session;
import cn.jbricks.toolkit.web.session.SessionHolder;
import cn.jbricks.toolkit.web.session.constant.SessionAttribute;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by kuiyuexiang on 15/12/25.
 */
public class SessionArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (SessionAttribute.userId.equals(parameter.getParameterName())) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Session session = SessionHolder.getSession();
        if (session == null) {
            return null;
        }

        if (SessionAttribute.userId.equals(parameter.getParameterName())
                && StringUtils.isNotEmpty(session.getUserId())) {
            return Long.valueOf(session.getUserId());
        }
        return null;
    }
}
