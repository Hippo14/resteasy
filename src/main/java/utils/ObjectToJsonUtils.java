package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class ObjectToJsonUtils {

    private static final Logger LOG = Logger.getLogger(ObjectToJsonUtils.class);

    public static String convertToJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;

        try {
            jsonString = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOG.error(e);
        }

        return jsonString;
    }

    public static <T> T convertToObject(String o, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        T object = null;

        try {
            object = (T) mapper.readValue(o, clazz);
        } catch (IOException e) {
            LOG.error(e);
        }

        return object;
    }
}
