package cn.jbricks.toolkit.web.rest.support;

import cn.jbricks.toolkit.clazz.ClassUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by kuiyuexiang on 15/12/25.
 */
public class JsonArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String jsonpCallback = "jsonpCallback";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !ClassUtil.isSimpleType(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String value = webRequest.getParameter(parameter.getParameterName());
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        if (StringUtils.isNotEmpty(webRequest.getParameter(jsonpCallback))
                && StringUtils.isNotEmpty(value)) {
            value = URLDecoder.decode(value, "utf-8");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (ClassUtil.isListType(parameter.getParameterType())) {
            Type types = parameter.getGenericParameterType();
            if (types != null && types instanceof ParameterizedType) {
                Class clazz = (Class) ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];
                return mapper.readValue(value, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
            }
        }
        return mapper.readValue(value, parameter.getParameterType());
    }
}
