package cn.jbricks.toolkit.web.rest.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Iterator;

/**
 * Created by kuiyuexiang on 15/12/25.
 */
public class ByteArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == byte[].class || parameter.getParameterType() == Byte[].class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (webRequest.getNativeRequest() instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) webRequest.getNativeRequest();
            Iterator<String> iterator = multipartRequest.getFileNames();
            if (iterator != null && iterator.hasNext()) {
                MultipartFile multipartFile = multipartRequest.getFile(iterator.next());
                if (multipartFile != null) {
                    return multipartFile.getBytes();
                }
            }
        }
        return null;
    }
}
