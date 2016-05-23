package lv.ctco.zephyr.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ObjectTransformer {
    private static final ObjectMapper mapper = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static <T> T deserialize(String response, Class<T> clazz) {
        JavaType typeRef = mapper.getTypeFactory().constructType(clazz);
        try {
            return mapper.readValue(response, typeRef);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> List<T> deserializeList(String response, Class<T> clazz) {
        JavaType typeRef = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return mapper.readValue(response, typeRef);
        } catch (IOException e) {
            return new ArrayList<T>();
        }
    }
}