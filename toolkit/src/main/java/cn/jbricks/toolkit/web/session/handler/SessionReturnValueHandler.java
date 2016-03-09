package cn.jbricks.toolkit.web.session.handler;

import cn.jbricks.toolkit.web.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by kuiyuexiang on 16/1/12.
 */
public class SessionReturnValueHandler implements HandlerMethodReturnValueHandler {

    private static final Logger logger = LoggerFactory.getLogger(SessionReturnValueHandler.class);

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        logger.debug("[supportsReturnType]");
        Type type = returnType.getGenericParameterType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getActualTypeArguments().length > 0) {
                type = parameterizedType.getActualTypeArguments()[0];
                Class clazz = (Class) ((ParameterizedType) type).getRawType();
                if (clazz == Session.class) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        logger.debug("[handleReturnValue]");
    }
}
