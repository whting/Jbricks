package cn.jbricks.toolkit.web.rest.converter;

import cn.jbricks.toolkit.result.TextResult;
import cn.jbricks.toolkit.web.session.SessionHolder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by kuiyuexiang on 15/11/18.
 */
public class MappingJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private static final String jsonpCallback = "jsonpCallback";

    public MappingJacksonHttpMessageConverter() {
        super();
        ObjectMapper objectMapper = getObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (TextResult.class == type) {
            TextResult textResult = (TextResult) object;
            if (textResult.getMessage() == null) {
                textResult.setMessage("");
            }
            outputMessage.getBody().write(textResult.getMessage().getBytes());
        } else {
            super.writeInternal(object, type, outputMessage);
        }
    }

    @Override
    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        if (SessionHolder.getSession() != null) {
            String jsonpFunction = SessionHolder.getSession().getRequest().getParameter(jsonpCallback);
            if (jsonpFunction != null) {
                generator.writeRaw("/**/");
                generator.writeRaw(jsonpFunction + "(");
            }
        }
    }

    @Override
    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
        if (SessionHolder.getSession() != null) {
            String jsonpFunction = SessionHolder.getSession().getRequest().getParameter(jsonpCallback);
            if (jsonpFunction != null) {
                generator.writeRaw(");");
            }
        }
    }
}
